package com.kubsu.timetable.data.gateway

import com.kubsu.timetable.*
import com.kubsu.timetable.data.db.timetable.*
import com.kubsu.timetable.data.mapper.UserDtoMapper
import com.kubsu.timetable.data.mapper.timetable.data.*
import com.kubsu.timetable.data.network.client.timetable.TimetableNetworkClient
import com.kubsu.timetable.data.network.client.university.UniversityDataNetworkClient
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.entity.timetable.data.*
import com.kubsu.timetable.domain.interactor.timetable.TimetableGateway
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map

class TimetableGatewayImpl(
    private val timetableQueries: TimetableQueries,
    private val classQueries: ClassQueries,
    private val classTimeQueries: ClassTimeQueries,
    private val lecturerQueries: LecturerQueries,
    private val universityInfoQueries: UniversityInfoQueries,
    private val timetableNetworkClient: TimetableNetworkClient,
    private val universityDataNetworkClient: UniversityDataNetworkClient
) : TimetableGateway {
    override suspend fun getUniversityData(
        facultyId: Int
    ): Either<DataFailure, UniversityInfoEntity> {
        val universityDbDto = universityInfoQueries
            .selectById(facultyId)
            .executeAsOneOrNull()

        return if (universityDbDto != null)
            Either.right(UniversityInfoDtoMapper.toEntity(universityDbDto))
        else
            universityDataNetworkClient
                .selectUniversityInfo(facultyId)
                .map { networkDto ->
                    universityInfoQueries.update(UniversityInfoDtoMapper.toDbDto(networkDto))
                    UniversityInfoDtoMapper.toEntity(networkDto)
                }
    }

    override suspend fun getAll(
        user: UserEntity,
        subgroupId: Int
    ): Either<DataFailure, List<TimetableEntity>> {
        val timetableDbList = timetableQueries
            .selectBySubgroupId(subgroupId)
            .executeAsList()

        return if (timetableDbList.isNotEmpty())
            timetableDbList
                .map(TimetableDtoMapper::toNetworkDto)
                .toTimetableEntityList()
        else
            timetableNetworkClient
                .selectTimetableListForUser(UserDtoMapper.toNetworkDto(user))
                .flatMap { list ->
                    list
                        .map(TimetableDtoMapper::toDbDto)
                        .forEach(timetableQueries::update)

                    list.toTimetableEntityList()
                }
    }

    private suspend fun List<TimetableNetworkDto>.toTimetableEntityList(): Either<DataFailure, List<TimetableEntity>> =
        flowOfIterable(this)
            .map { timetable ->
                selectClassList(timetable.id).map { TimetableDtoMapper.toEntity(timetable, it) }
            }
            .collectRightListOrFirstLeft()

    private suspend fun selectClassList(timetableId: Int): Either<DataFailure, List<ClassEntity>> {
        val classDbList = classQueries
            .selectByTimetableId(timetableId)
            .executeAsList()

        return if (classDbList.isNotEmpty())
            classDbList
                .map(ClassDtoMapper::toNetworkDto)
                .toClassEntityList()
        else
            timetableNetworkClient
                .selectClassesByTimetableId(timetableId)
                .flatMap { list ->
                    list
                        .map(ClassDtoMapper::toDbDto)
                        .forEach(classQueries::update)

                    list.toClassEntityList()
                }
    }

    private suspend fun List<ClassNetworkDto>.toClassEntityList(): Either<DataFailure, List<ClassEntity>> =
        coroutineScope {
            flowOfIterable(this@toClassEntityList)
                .map { clazz ->
                    val classTimeDef = async { selectClassTime(clazz.classTimeId) }
                    val lecturerDef = async { selectLecturer(clazz.lecturerId) }

                    classTimeDef
                        .await()
                        .also { if (it is Either.Left) lecturerDef.cancel() }
                        .flatMap { classTime ->
                            lecturerDef
                                .await()
                                .map { lecturer ->
                                    ClassDtoMapper.toEntity(clazz, classTime, lecturer)
                                }
                        }
                }
                .collectRightListOrFirstLeft()
        }

    private suspend fun selectClassTime(id: Int): Either<DataFailure, ClassTimeEntity> {
        val classTime = classTimeQueries
            .selectById(id)
            .executeAsOneOrNull()

        return if (classTime != null)
            Either.right(ClassTimeDtoMapper.toEntity(classTime))
        else
            timetableNetworkClient
                .selectClassTimeById(id)
                .map {
                    classTimeQueries.update(ClassTimeDtoMapper.toDbDto(it))
                    ClassTimeDtoMapper.toEntity(it)
                }
    }

    private suspend fun selectLecturer(id: Int): Either<DataFailure, LecturerEntity> {
        val lecturer = lecturerQueries
            .selectById(id)
            .executeAsOneOrNull()

        return if (lecturer != null)
            Either.right(LecturerDtoMapper.toEntity(lecturer))
        else
            timetableNetworkClient
                .selectLecturerById(id)
                .map {
                    lecturerQueries.update(LecturerDtoMapper.toDbDto(it))
                    LecturerDtoMapper.toEntity(it)
                }
    }
}