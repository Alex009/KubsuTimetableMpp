package com.kubsu.timetable.domain.interactor.sync

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import kotlinx.coroutines.flow.Flow

interface SyncMixinInteractor {
    suspend fun registerDataDiff(entity: DataDiffEntity.Raw)

    suspend fun syncDataAndObserveUpdates(): Flow<Either<DataFailure, Unit>>

    suspend fun syncData(): Either<DataFailure, Unit>
}