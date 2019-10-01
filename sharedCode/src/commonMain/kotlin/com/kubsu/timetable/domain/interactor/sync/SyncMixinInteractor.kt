package com.kubsu.timetable.domain.interactor.sync

import com.kubsu.timetable.Either
import com.kubsu.timetable.NoActiveUserFailure
import com.kubsu.timetable.WrapperFailure

interface SyncMixinInteractor {
    suspend fun updateData(): Either<WrapperFailure<NoActiveUserFailure>, Unit>
}