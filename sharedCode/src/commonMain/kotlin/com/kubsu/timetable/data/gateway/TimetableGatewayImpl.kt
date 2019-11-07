package com.kubsu.timetable.data.gateway

import com.kubsu.timetable.data.db.timetable.*
import com.kubsu.timetable.data.mapper.timetable.data.*
import com.kubsu.timetable.domain.entity.timetable.data.*
import com.kubsu.timetable.domain.interactor.timetable.TimetableGateway
import com.kubsu.timetable.extensions.getWeekNumber
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
    override fun getUniversityData(facultyId: Int): Flow<UniversityInfoEntity> {
        val query = universityInfoQueries.selectByFacultyId(facultyId)
        val universityInfo = query.executeAsOne()
        val weekNumber = getWeekNumber()
        if (universityInfo.weekNumber != weekNumber)
            universityInfo.actualize(weekNumber)
        return query
            .asFlow()
            .mapToOne()
            .map { UniversityInfoDtoMapper.toEntity(it) }
    }

    private fun UniversityInfoDb.actualize(newWeekNumber: Int) =
        universityInfoQueries.update(
            UniversityInfoDb.Impl(
                id = id,
                facultyId = facultyId,
                typeOfWeek = inverseTypeOfWeek(typeOfWeek),
                weekNumber = newWeekNumber
            )
        )

    private fun inverseTypeOfWeek(typeOfWeek: Int): Int =
        TypeOfWeek
            .list
            .minus(TypeOfWeekDtoMapper.toEntity(typeOfWeek))
            .first()
            .let(TypeOfWeekDtoMapper::value)

    @UseExperimental(ExperimentalCoroutinesApi::class)
    override fun getAllTimetablesFlow(
        subgroupId: Int
    ): Flow<List<TimetableEntity>> =
        timetableQueries
            .selectBySubgroupId(subgroupId)
            .asFlow()
            .mapToList()
            .flatMapLatest(::toTimetableEntityList)

    @UseExperimental(ExperimentalCoroutinesApi::class)
    private suspend fun toTimetableEntityList(
        timetableList: List<TimetableDb>
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
            .flatMapLatest(::toClassEntityListFlow)

    @UseExperimental(ExperimentalCoroutinesApi::class)
    private suspend fun toClassEntityListFlow(
        classList: List<ClassDb>
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

    override suspend fun changesWasDisplayed(clazz: ClassEntity) {
        val classDb = classQueries.selectById(clazz.id).executeAsOne()
        if (classDb.needToEmphasize)
            classQueries.update(ClassDtoMapper.toDbDto(clazz.copy(needToEmphasize = false)))
    }
}