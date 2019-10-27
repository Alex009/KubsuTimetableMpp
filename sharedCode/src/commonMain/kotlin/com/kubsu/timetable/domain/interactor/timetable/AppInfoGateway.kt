package com.kubsu.timetable.domain.interactor.timetable

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure

interface AppInfoGateway {
    suspend fun updateInfo(userId: Int): Either<DataFailure, Unit>
}