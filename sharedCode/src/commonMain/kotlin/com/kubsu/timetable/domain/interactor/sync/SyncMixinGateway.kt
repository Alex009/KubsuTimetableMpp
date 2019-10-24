package com.kubsu.timetable.domain.interactor.sync

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import kotlinx.coroutines.flow.Flow

interface SyncMixinGateway {
    suspend fun registerDataDiff(entity: DataDiffEntity)
    fun getAvailableDiffListFlowForCurrentUser(): Flow<List<DataDiffEntity>>
    suspend fun delete(list: List<DataDiffEntity>)
    suspend fun diff(): Either<DataFailure, Pair<Timestamp, List<Basename>>>

    suspend fun updateData(
        basename: Basename,
        availableDiff: DataDiffEntity
    ): Either<DataFailure, Unit>

    suspend fun meta(
        basename: Basename,
        updatedIds: List<Int>
    ): Either<DataFailure, Unit>
}