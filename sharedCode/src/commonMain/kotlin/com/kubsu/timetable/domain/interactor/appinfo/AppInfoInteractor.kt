package com.kubsu.timetable.domain.interactor.appinfo

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure

interface AppInfoInteractor {
    suspend fun invalidate(): Either<DataFailure, Unit>
}