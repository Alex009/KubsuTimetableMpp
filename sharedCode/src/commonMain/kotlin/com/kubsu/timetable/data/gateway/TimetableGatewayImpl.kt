package com.kubsu.timetable.data.gateway

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.db.timetable.ClassQueries
import com.kubsu.timetable.data.db.timetable.ClassTimeQueries
import com.kubsu.timetable.data.db.timetable.LecturerQueries
import com.kubsu.timetable.data.db.timetable.TimetableQueries
import com.kubsu.timetable.data.mapper.timetable.data.ClassMapper
import com.kubsu.timetable.data.mapper.timetable.data.ClassTimeMapper
import com.kubsu.timetable.data.mapper.timetable.data.LecturerMapper
import com.kubsu.timetable.data.mapper.timetable.data.TimetableMapper
import com.kubsu.timetable.data.network.NetworkClient
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto
import com.kubsu.timetable.domain.entity.timetable.data.ClassEntity
import com.kubsu.timetable.domain.entity.timetable.data.ClassTimeEntity
import com.kubsu.timetable.domain.entity.timetable.data.LecturerEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.domain.interactor.timetable.TimetableGateway
import com.kubsu.timetable.flatMap
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class TimetableGatewayImpl(
    private val timetableQueries: TimetableQueries,
    private val classQueries: ClassQueries,
    private val classTimeQueries: ClassTimeQueries,
    private val lecturerQueries: LecturerQueries,
    private val networkClient: NetworkClient
) : TimetableGateway {
    override suspend fun getAllTimetables(
        subgroupId: Int
    ): Either<NetworkFailure, List<TimetableEntity>> {
        val timetableDbList = timetableQueries
            .selectBySubgroupId(subgroupId)
            .executeAsList()

        return if (timetableDbList.isNotEmpty())
            timetableDbList
                .map(TimetableMapper::toNetworkDto)
                .toTimetableEntityList()
        else
            networkClient
                .selectTimetableListForUser()
                .flatMap { list ->
                    list
                        .map(TimetableMapper::toDbDto)
                        .forEach(timetableQueries::update)

                    list.toTimetableEntityList()
                }
    }

    private suspend fun List<TimetableNetworkDto>.toTimetableEntityList(): Either<NetworkFailure, List<TimetableEntity>> {
        return Either.right(
            map { timetable ->
                val classList = selectClassList(timetable.id)
                    .fold(
                        ifLeft = { fail -> return Either.left(fail) },
                        ifRight = { classList -> classList }
                    )
                TimetableMapper.toEntity(timetable, classList)
            }
        )
    }

    private suspend fun selectClassList(timetableId: Int): Either<NetworkFailure, List<ClassEntity>> {
        val classDbList = classQueries
            .selectByTimetableId(timetableId)
            .executeAsList()

        return if (classDbList.isNotEmpty())
            classDbList
                .map(ClassMapper::toNetworkDto)
                .toClassEntityList()
        else
            networkClient
                .selectClassesByTimetableId(timetableId)
                .flatMap { list ->
                    list
                        .map { ClassMapper.toDbDto(it) }
                        .forEach(classQueries::update)

                    list.toClassEntityList()
                }
    }

    private suspend fun List<ClassNetworkDto>.toClassEntityList(): Either<NetworkFailure, List<ClassEntity>> =
        coroutineScope {
            Either.right(
                map { clazz ->
                    val classTimeDef = async { selectClassTime(clazz.classTimeId) }
                    val lecturerDef = async { selectLecturer(clazz.lecturerId) }

                    val classTime = classTimeDef
                        .await()
                        .fold(
                            ifLeft = { fail -> return@coroutineScope Either.left(fail) },
                            ifRight = { classTimeEntity -> classTimeEntity }
                        )
                    val lecturer = lecturerDef
                        .await()
                        .fold(
                            ifLeft = { fail -> return@coroutineScope Either.left(fail) },
                            ifRight = { lecturerEntity -> lecturerEntity }
                        )
                    ClassMapper.toEntity(clazz, classTime, lecturer)
                }
            )
        }

    private suspend fun selectClassTime(id: Int): Either<NetworkFailure, ClassTimeEntity> {
        val classTime = classTimeQueries
            .selectById(id)
            .executeAsOneOrNull()

        return if (classTime != null)
            Either.right(ClassTimeMapper.toEntity(classTime))
        else
            networkClient
                .selectClassTimeById(id)
                .map {
                    classTimeQueries.update(ClassTimeMapper.toDbDto(it))
                    ClassTimeMapper.toEntity(it)
                }
    }

    private suspend fun selectLecturer(id: Int): Either<NetworkFailure, LecturerEntity> {
        val lecturer = lecturerQueries
            .selectById(id)
            .executeAsOneOrNull()

        return if (lecturer != null)
            Either.right(LecturerMapper.toEntity(lecturer))
        else
            networkClient
                .selectLecturerById(id)
                .map {
                    lecturerQueries.update(LecturerMapper.toDbDto(it))
                    LecturerMapper.toEntity(it)
                }
    }
}