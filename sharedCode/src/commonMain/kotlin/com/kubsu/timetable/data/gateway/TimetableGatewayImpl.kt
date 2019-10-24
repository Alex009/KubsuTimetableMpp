package com.kubsu.timetable.data.gateway

import com.egroden.teaco.*
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.db.timetable.*
import com.kubsu.timetable.data.mapper.timetable.data.*
import com.kubsu.timetable.data.network.client.timetable.TimetableNetworkClient
import com.kubsu.timetable.data.network.client.university.UniversityDataNetworkClient
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto
import com.kubsu.timetable.domain.entity.timetable.data.*
import com.kubsu.timetable.domain.interactor.timetable.TimetableGateway
import com.kubsu.timetable.extensions.asFilteredFlow
import com.kubsu.timetable.extensions.toEitherList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class TimetableGatewayImpl(
    private val timetableQueries: TimetableQueries,
    private val classQueries: ClassQueries,
    private val classTimeQueries: ClassTimeQueries,
    private val lecturerQueries: LecturerQueries,
    private val universityInfoQueries: UniversityInfoQueries,
    private val timetableNetworkClient: TimetableNetworkClient,
    private val universityDataNetworkClient: UniversityDataNetworkClient
) : TimetableGateway {
    @UseExperimental(ExperimentalCoroutinesApi::class)
    override fun getUniversityData(facultyId: Int): Flow<Either<DataFailure, UniversityInfoEntity>> =
        universityInfoQueries
            .selectByFacultyId(facultyId)
            .asFilteredFlow { query -> query.executeAsOneOrNull() }
            .map { universityDbDto ->
                if (universityDbDto != null)
                    Either.right(UniversityInfoDtoMapper.toEntity(universityDbDto))
                else
                    universityDataNetworkClient
                        .selectUniversityInfo(facultyId)
                        .map { networkDto ->
                            universityInfoQueries.update(UniversityInfoDtoMapper.toDbDto(networkDto))
                            UniversityInfoDtoMapper.toEntity(networkDto)
                        }
            }

    @UseExperimental(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override fun getAllTimetablesFlow(
        subgroupId: Int
    ): Flow<Either<DataFailure, List<TimetableEntity>>> =
        timetableQueries
            .selectBySubgroupId(subgroupId)
            .asFilteredFlow { query -> query.executeAsList() }
            .flatMapConcat { timetableDbList ->
                if (timetableDbList.isNotEmpty())
                    timetableDbList
                        .map(TimetableDtoMapper::toNetworkDto)
                        .toTimetableEntityList()
                else
                    timetableNetworkClient
                        .selectTimetableList(subgroupId)
                        .fold(
                            ifLeft = { flowOf(Either.left(it)) },
                            ifRight = { list ->
                                list
                                    .map(TimetableDtoMapper::toDbDto)
                                    .forEach(timetableQueries::update)

                                list.toTimetableEntityList()
                            }
                        )
            }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    private fun List<TimetableNetworkDto>.toTimetableEntityList(): Flow<Either<DataFailure, List<TimetableEntity>>> {
        val flows = map { timetable ->
            selectClassList(timetable.id).map { either ->
                either.map { TimetableDtoMapper.toEntity(timetable, it) }
            }
        }
        return combine(flows, transform = { it.toList().toEitherList() })
    }

    @UseExperimental(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun selectClassList(timetableId: Int): Flow<Either<DataFailure, List<ClassEntity>>> =
        classQueries
            .selectByTimetableId(timetableId)
            .asFilteredFlow { query -> query.executeAsList() }
            .flatMapConcat { classDbList ->
                if (classDbList.isNotEmpty())
                    classDbList
                        .map(ClassDtoMapper::toNetworkDto)
                        .toClassEntityListFlow()
                else
                    timetableNetworkClient
                        .selectClassesByTimetableId(timetableId)
                        .fold(
                            ifLeft = { flowOf(Either.left(it)) },
                            ifRight = { list ->
                                list
                                    .map(ClassDtoMapper::toDbDto)
                                    .forEach(classQueries::update)

                                list.toClassEntityListFlow()
                            }
                        )
            }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    private fun List<ClassNetworkDto>.toClassEntityListFlow(): Flow<Either<DataFailure, List<ClassEntity>>> {
        val flows = map { clazz ->
            selectClassTimeFlow(clazz.classTimeId)
                .combine(selectLecturerFlow(clazz.lecturerId)) { classTimeEither, lecturerEither ->
                    classTimeEither.flatMap { classTime ->
                        lecturerEither.map { lecturer ->
                            ClassDtoMapper.toEntity(clazz, classTime, lecturer)
                        }
                    }
                }
        }
        return combine(flows, transform = { it.toList().toEitherList() })
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    private fun selectClassTimeFlow(id: Int): Flow<Either<DataFailure, ClassTimeEntity>> =
        classTimeQueries
            .selectById(id)
            .asFilteredFlow { query -> query.executeAsOneOrNull() }
            .map { classTime ->
                if (classTime != null)
                    Either.right(ClassTimeDtoMapper.toEntity(classTime))
                else
                    timetableNetworkClient
                        .selectClassTimeById(id)
                        .map {
                            classTimeQueries.update(ClassTimeDtoMapper.toDbDto(it))
                            ClassTimeDtoMapper.toEntity(it)
                        }
            }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    private fun selectLecturerFlow(id: Int): Flow<Either<DataFailure, LecturerEntity>> =
        lecturerQueries
            .selectById(id)
            .asFilteredFlow { query -> query.executeAsOneOrNull() }
            .map { lecturer ->
                if (lecturer != null)
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