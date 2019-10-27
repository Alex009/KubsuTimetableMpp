package com.kubsu.timetable.data.gateway

import com.kubsu.timetable.data.db.timetable.*
import com.kubsu.timetable.data.mapper.timetable.data.*
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto
import com.kubsu.timetable.domain.entity.timetable.data.*
import com.kubsu.timetable.domain.interactor.timetable.TimetableGateway
import com.kubsu.timetable.extensions.getContentFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map

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
            .getContentFlow { query -> query.executeAsOne() }
            .map { UniversityInfoDtoMapper.toEntity(it) }

    @UseExperimental(FlowPreview::class)
    override fun getAllTimetablesFlow(
        subgroupId: Int
    ): Flow<List<TimetableEntity>> =
        timetableQueries
            .selectBySubgroupId(subgroupId)
            .getContentFlow { query -> query.executeAsList() }
            .map { it.map(TimetableDtoMapper::toNetworkDto) }
            .flatMapMerge { toTimetableEntityList(it) }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    private fun toTimetableEntityList(
        timetableList: List<TimetableNetworkDto>
    ): Flow<List<TimetableEntity>> {
        val flows = timetableList.map { timetable ->
            selectClassList(timetable.id).map {
                TimetableDtoMapper.toEntity(timetable, it)
            }
        }
        return combine(flows, transform = { it.toList() })
    }

    @UseExperimental(FlowPreview::class)
    private fun selectClassList(
        timetableId: Int
    ): Flow<List<ClassEntity>> =
        classQueries.selectByTimetableId(timetableId)
            .getContentFlow { it.executeAsList() }
            .map { it.map(ClassDtoMapper::toNetworkDto) }
            .flatMapMerge { toClassEntityListFlow(it) }

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
        return combine(flows) { it.toList() }
    }

    private fun selectClassTimeFlow(id: Int): Flow<ClassTimeEntity> =
        classTimeQueries
            .selectById(id)
            .getContentFlow { it.executeAsOne() }
            .map { ClassTimeDtoMapper.toEntity(it) }

    private fun selectLecturerFlow(id: Int): Flow<LecturerEntity> =
        lecturerQueries
            .selectById(id)
            .getContentFlow { it.executeAsOne() }
            .map { LecturerDtoMapper.toEntity(it) }
}