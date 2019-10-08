package com.kubsu.timetable.domain.interactor.sync

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.Either

interface SyncMixinInteractor {
    suspend fun updateData(): Either<DataFailure, Unit>
}