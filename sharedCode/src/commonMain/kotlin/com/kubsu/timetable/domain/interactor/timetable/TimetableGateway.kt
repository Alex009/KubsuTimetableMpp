package com.kubsu.timetable.domain.interactor.timetable

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.domain.entity.timetable.data.UniversityInfoEntity
import kotlinx.coroutines.flow.Flow

interface TimetableGateway {
    fun getUniversityData(facultyId: Int): Flow<Either<DataFailure, UniversityInfoEntity>>

    fun getAllTimetablesFlow(
        subgroupId: Int
    ): Flow<Either<DataFailure, List<TimetableEntity>>>
}