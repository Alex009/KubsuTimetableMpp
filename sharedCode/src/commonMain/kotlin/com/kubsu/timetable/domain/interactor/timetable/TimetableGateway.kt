package com.kubsu.timetable.domain.interactor.timetable

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.domain.entity.MainInfoEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity

interface TimetableGateway {
    suspend fun getMainInfo(): Either<NetworkFailure, MainInfoEntity>

    suspend fun getAllTimetables(subgroupId: Int): Either<NetworkFailure, List<TimetableEntity>>
}