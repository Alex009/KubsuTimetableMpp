package com.kubsu.timetable.domain.interactor.sync

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity

interface SyncMixinGateway {
    fun registerDataDiff(entity: DataDiffEntity)
    suspend fun getAvailableDiffList(userId: Int): List<DataDiffEntity>
    suspend fun delete(list: List<DataDiffEntity>)
    suspend fun diff(user: UserEntity): Either<DataFailure, Pair<Timestamp, List<Basename>>>

    suspend fun updateData(
        basename: Basename,
        availableDiff: DataDiffEntity,
        user: UserEntity
    ): Either<DataFailure, Unit>

    suspend fun meta(
        basename: Basename,
        user: UserEntity,
        updatedIds: List<Int>
    ): Either<DataFailure, Unit>
}