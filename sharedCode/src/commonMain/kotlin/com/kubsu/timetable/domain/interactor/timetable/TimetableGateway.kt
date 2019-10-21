package com.kubsu.timetable.domain.interactor.timetable

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.Either
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.domain.entity.timetable.data.UniversityInfoEntity

interface TimetableGateway {
    suspend fun getUniversityData(facultyId: Int): Either<DataFailure, UniversityInfoEntity>

    suspend fun getAll(
        subgroupId: Int,
        user: UserEntity
    ): Either<DataFailure, List<TimetableEntity>>
}