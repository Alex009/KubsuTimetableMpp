package com.kubsu.timetable.domain.interactor.timetable

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.Either
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity

interface TimetableGateway {
    suspend fun getAll(subgroupId: Int): Either<DataFailure, List<TimetableEntity>>
}