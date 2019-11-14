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
import com.kubsu.timetable.data.storage.user.session.Session
import com.kubsu.timetable.domain.interactor.appinfo.AppInfoGateway
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
    override suspend fun updateInfo(session: Session, userId: Int): Either<DataFailure, Unit> {
        val currentList = subscriptionQueries
            .selectByUserId(userId)
            .executeAsList()
            .map(SubscriptionDtoMapper::toNetworkDto)
        return if (currentList.isEmpty())
            subscriptionNetworkClient
                .selectSubscriptionsForUser(session)
                .flatMap {
                    for (networkDto in it)
                        subscriptionQueries.update(
                            SubscriptionDtoMapper.toDbDto(networkDto)
                        )
                    checkSubscriptionDependencies(it)
                }
        else
            checkSubscriptionDependencies(currentList)
    }

    override suspend fun checkSubscriptionDependencies(
        list: List<SubscriptionNetworkDto>
    ): Either<DataFailure, Unit> =
        list
            .map { checkAvailabilityOfTimetables(it) }
            .toEitherList()
            .map { Unit }

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
                    for (networkDto in it)
                        timetableQueries.update(
                            TimetableDtoMapper.toDbDto(networkDto)
                        )
                    checkTimetableDependencies(it)
                }
        else
            checkTimetableDependencies(currentList)
    }

    override suspend fun checkTimetableDependencies(
        list: List<TimetableNetworkDto>
    ): Either<DataFailure, Unit> =
        list
            .map { timetable ->
                checkAvailabilityOfClasses(timetable).flatMap {
                    checkAvailabilityOfUniversityInfo(timetable)
                }
            }
            .toEitherList()
            .map { Unit }

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

    suspend fun checkAvailabilityOfClasses(
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
                    for (networkDto in it)
                        classQueries.update(
                            ClassDtoMapper.toDbDto(networkDto, needToEmphasize = false)
                        )
                    checkClassDependencies(it)
                }
        else
            checkClassDependencies(currentList)
    }

    override suspend fun checkClassDependencies(
        list: List<ClassNetworkDto>
    ): Either<DataFailure, Unit> =
        list
            .map { clazz ->
                checkAvailabilityOfClassTime(clazz)
            }
            .toEitherList()
            .map { Unit }

    suspend fun checkAvailabilityOfClassTime(clazz: ClassNetworkDto): Either<DataFailure, Unit> {
        val id = clazz.classTimeId
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

    suspend fun checkAvailabilityOfLecturer(clazz: ClassNetworkDto): Either<DataFailure, Unit> {
        val id = clazz.lecturerId
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

    override suspend fun clearUserInfo(userId: Int) {
        val subscriptions = subscriptionQueries.selectByUserId(userId).executeAsList()
        for (subscription in subscriptions) {
            subscriptionQueries.deleteById(subscription.id)
            removeAllDependencies(subscription)
        }
    }

    fun removeAllDependencies(subscription: SubscriptionDb) {
        val timetables =
            timetableQueries.selectBySubgroupId(subscription.subgroupId).executeAsList()
        for (timetable in timetables) {
            timetableQueries.deleteById(timetable.id)
            removeAllDependencies(timetable)
        }
    }

    private fun removeAllDependencies(timetable: TimetableDb) {
        universityInfoQueries.deleteByFacultyId(timetable.facultyId)

        val classes = classQueries.selectByTimetableId(timetable.id).executeAsList()
        for (clazz in classes) {
            classQueries.deleteById(clazz.id)
            removeAllDependencies(clazz)
        }
    }

    private fun removeAllDependencies(clazz: ClassDb) {
        classTimeQueries.deleteById(clazz.classTimeId)
        lecturerQueries.deleteById(clazz.lecturerId)
    }
}