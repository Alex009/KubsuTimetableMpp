package com.kubsu.timetable.domain.interactor.sync

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure

interface SyncMixinInteractor {
    suspend fun updateData(): Either<DataFailure, Unit>
}