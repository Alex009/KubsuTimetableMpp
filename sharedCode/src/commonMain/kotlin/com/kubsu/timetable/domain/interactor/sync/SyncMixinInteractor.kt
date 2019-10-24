package com.kubsu.timetable.domain.interactor.sync

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import kotlinx.coroutines.flow.Flow

interface SyncMixinInteractor {
    suspend fun registerDataDiff(entity: DataDiffEntity)

    fun updateData(): Flow<Either<DataFailure, Unit>>
}