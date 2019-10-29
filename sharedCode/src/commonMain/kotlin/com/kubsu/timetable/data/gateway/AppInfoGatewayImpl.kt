package com.kubsu.timetable.data.gateway

import com.egroden.teaco.Either
import com.egroden.teaco.flatMap
import com.egroden.teaco.map
import com.egroden.teaco.right
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.db.timetable.*
import com.kubsu.timetable.data.mapper.timetable.data.*
import com.kubsu.timetable.data.network.client.subscription.SubscriptionNetworkClient
import com.kubsu.timetable.data.network.client.timetable.TimetableNetworkClient
import com.kubsu.timetable.data.network.client.university.UniversityDataNetworkClient
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.SubscriptionNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto
import com.kubsu.timetable.domain.interactor.timetable.AppInfoGateway
import com.kubsu.timetable.extensions.toEitherList

class AppInfoGatewayImpl(
    private val classQueries: ClassQueries,
    private val classTimeQueries: ClassTimeQueries,
    private val lecturerQueries: LecturerQueries,
    private val subscriptionQueries: SubscriptionQueries,
    private val timetableQueries: TimetableQueries,
    private val universityInfoQueries: UniversityInfoQueries,
    private val subscriptionNetworkClient: SubscriptionNetworkClient,
    private val timetableNetworkClient: TimetableNetworkClient,
    private val universityDataNetworkClient: UniversityDataNetworkClient
) : AppInfoGateway {
    override suspend fun updateInfo(userId: Int): Either<DataFailure, Unit> {
        val currentList = subscriptionQueries
            .selectByUserId(userId)
            .executeAsList()
            .map(SubscriptionDtoMapper::toNetworkDto)
        return if (currentList.isEmpty())
            subscriptionNetworkClient
                .selectSubscriptionsForUser()
                .flatMap {
                    checkSubscriptionDependencies(it).map { Unit }
                }
        else
            checkSubscriptionDependencies(currentList).map { Unit }
    }

    private suspend fun checkSubscriptionDependencies(
        list: List<SubscriptionNetworkDto>
    ): Either<DataFailure, List<Unit>> =
        list
            .map { subscription ->
                checkAvailabilityOfTimetables(subscription).map {
                    subscriptionQueries.update(
                        SubscriptionDtoMapper.toDbDto(subscription)
                    )
                }
            }
            .toEitherList()

    private suspend fun checkAvailabilityOfTimetables(
        subscription: SubscriptionNetworkDto
    ): Either<DataFailure, Unit> {
        val subgroupId = subscription.subgroup
        val currentList = timetableQueries
            .selectBySubgroupId(subgroupId)
            .executeAsList()
            .map(TimetableDtoMapper::toNetworkDto)
        return if (currentList.isEmpty())
            timetableNetworkClient
                .selectTimetableList(subgroupId)
                .flatMap {
                    checkTimetableDependencies(it).map { Unit }
                }
        else
            checkTimetableDependencies(currentList).map { Unit }
    }

    private suspend fun checkTimetableDependencies(
        list: List<TimetableNetworkDto>
    ): Either<DataFailure, List<Unit>> =
        list
            .map { timetable ->
                checkAvailabilityOfClasses(timetable).flatMap {
                    checkAvailabilityOfUniversityInfo(timetable).map {
                        timetableQueries.update(
                            TimetableDtoMapper.toDbDto(timetable)
                        )
                    }
                }
            }
            .toEitherList()

    private suspend fun checkAvailabilityOfUniversityInfo(
        timetable: TimetableNetworkDto
    ): Either<DataFailure, Unit> {
        val facultyId = timetable.facultyId
        val currentInfo = universityInfoQueries.selectByFacultyId(facultyId).executeAsOneOrNull()
        return if (currentInfo == null)
            universityDataNetworkClient
                .selectUniversityInfo(facultyId)
                .map {
                    universityInfoQueries.update(UniversityInfoDtoMapper.toDbDto(it))
                }
        else
            Either.right(Unit)
    }

    private suspend fun checkAvailabilityOfClasses(
        timetable: TimetableNetworkDto
    ): Either<DataFailure, Unit> {
        val timetableId = timetable.id
        val currentList = classQueries
            .selectByTimetableId(timetableId)
            .executeAsList()
            .map(ClassDtoMapper::toNetworkDto)
        return if (currentList.isEmpty())
            timetableNetworkClient
                .selectClassesByTimetableId(timetableId)
                .flatMap {
                    checkClassDependencies(it).map { Unit }
                }
        else
            checkClassDependencies(currentList).map { Unit }
    }

    private suspend fun checkClassDependencies(
        list: List<ClassNetworkDto>
    ): Either<DataFailure, List<Unit>> =
        list
            .map { clazz ->
                checkAvailabilityOfClassTime(clazz.classTimeId).flatMap {
                    checkAvailabilityOfLecturer(clazz.lecturerId).map {
                        classQueries.update(ClassDtoMapper.toDbDto(clazz))
                    }
                }
            }
            .toEitherList()

    private suspend fun checkAvailabilityOfClassTime(id: Int): Either<DataFailure, Unit> {
        val currentClassTime = classTimeQueries.selectById(id).executeAsOneOrNull()
        return if (currentClassTime == null)
            timetableNetworkClient
                .selectClassTimeById(id)
                .map {
                    classTimeQueries.update(ClassTimeDtoMapper.toDbDto(it))
                }
        else
            Either.right(Unit)
    }

    private suspend fun checkAvailabilityOfLecturer(id: Int): Either<DataFailure, Unit> {
        val currentLecturer = lecturerQueries.selectById(id).executeAsOneOrNull()
        return if (currentLecturer == null)
            timetableNetworkClient
                .selectLecturerById(id)
                .map {
                    lecturerQueries.update(LecturerDtoMapper.toDbDto(it))
                }
        else
            Either.right(Unit)
    }
}