package com.kubsu.timetable.domain.interactor.timetable

import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.domain.entity.timetable.data.UniversityInfoEntity
import kotlinx.coroutines.flow.Flow

interface TimetableGateway {
    fun getUniversityData(facultyId: Int): Flow<UniversityInfoEntity>
    fun getAllTimetablesFlow(subgroupId: Int): Flow<List<TimetableEntity>>
}