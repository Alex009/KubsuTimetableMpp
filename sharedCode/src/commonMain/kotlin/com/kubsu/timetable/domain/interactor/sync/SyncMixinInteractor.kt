package com.kubsu.timetable.domain.interactor.sync

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity

interface SyncMixinInteractor {
    suspend fun registerDataDiff(entity: DataDiffEntity)

    suspend fun updateData(): Either<DataFailure, Unit>
}