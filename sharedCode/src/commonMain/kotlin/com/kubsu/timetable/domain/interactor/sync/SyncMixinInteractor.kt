package com.kubsu.timetable.domain.interactor.sync

import com.kubsu.timetable.Either
import com.kubsu.timetable.NoActiveUserFailure
import com.kubsu.timetable.RequestFailure

interface SyncMixinInteractor {
    suspend fun updateData(): Either<RequestFailure<NoActiveUserFailure>, Unit>
}