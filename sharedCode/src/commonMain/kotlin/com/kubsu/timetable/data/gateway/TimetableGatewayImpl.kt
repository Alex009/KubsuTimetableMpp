package com.kubsu.timetable.data.gateway

import com.kubsu.timetable.data.db.timetable.*
import com.kubsu.timetable.data.mapper.timetable.data.*
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto
import com.kubsu.timetable.domain.entity.timetable.data.*
import com.kubsu.timetable.domain.interactor.timetable.TimetableGateway
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class TimetableGatewayImpl(
    private val timetableQueries: TimetableQueries,
    private val classQueries: ClassQueries,
    private val classTimeQueries: ClassTimeQueries,
    private val lecturerQueries: LecturerQueries,
    private val universityInfoQueries: UniversityInfoQueries
) : TimetableGateway {
    override fun getUniversityData(facultyId: Int): Flow<UniversityInfoEntity> =
        universityInfoQueries
            .selectByFacultyId(facultyId)
            .asFlow()
            .mapToOne()
            .map { UniversityInfoDtoMapper.toEntity(it) }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    override fun getAllTimetablesFlow(
        subgroupId: Int
    ): Flow<List<TimetableEntity>> =
        timetableQueries
            .selectBySubgroupId(subgroupId)
            .asFlow()
            .mapToList()
            .map { it.map(TimetableDtoMapper::toNetworkDto) }
            .flatMapLatest { toTimetableEntityList(it) }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    private fun toTimetableEntityList(
        timetableList: List<TimetableNetworkDto>
    ): Flow<List<TimetableEntity>> {
        val flows = timetableList.map { timetable ->
            selectClassList(timetable.id).map {
                TimetableDtoMapper.toEntity(timetable, it)
            }
        }
        return if (flows.isNotEmpty())
            combine(flows, transform = { it.toList() })
        else
            flowOf(emptyList())
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    private fun selectClassList(
        timetableId: Int
    ): Flow<List<ClassEntity>> =
        classQueries
            .selectByTimetableId(timetableId)
            .asFlow()
            .mapToList()
            .map { it.map(ClassDtoMapper::toNetworkDto) }
            .flatMapLatest { toClassEntityListFlow(it) }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    private fun toClassEntityListFlow(
        classList: List<ClassNetworkDto>
    ): Flow<List<ClassEntity>> {
        val flows = classList.map { clazz ->
            selectClassTimeFlow(clazz.classTimeId)
                .combine(selectLecturerFlow(clazz.lecturerId)) { classTime, lecturer ->
                    ClassDtoMapper.toEntity(clazz, classTime, lecturer)
                }
        }
        return if (flows.isNotEmpty())
            combine(flows) { it.toList() }
        else
            flowOf(emptyList())
    }

    private fun selectClassTimeFlow(id: Int): Flow<ClassTimeEntity> =
        classTimeQueries
            .selectById(id)
            .asFlow()
            .mapToOne()
            .map { ClassTimeDtoMapper.toEntity(it) }

    private fun selectLecturerFlow(id: Int): Flow<LecturerEntity> =
        lecturerQueries
            .selectById(id)
            .asFlow()
            .mapToOne()
            .map { LecturerDtoMapper.toEntity(it) }
}