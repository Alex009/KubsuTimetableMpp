package com.kubsu.timetable.domain.interactor.sync

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.storage.user.session.Session
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import kotlinx.coroutines.flow.Flow

interface SyncMixinGateway {
    suspend fun registerDataDiff(entity: DataDiffEntity)

    fun getAvailableDiffList(): List<DataDiffEntity>
    fun dataDiffListFlow(): Flow<List<DataDiffEntity>>
    fun deleteBasenameData(basename: Basename, deletedIds: List<Int>)
    suspend fun delete(list: List<DataDiffEntity>)

    suspend fun diff(session: Session): Either<DataFailure, Pair<Timestamp, List<Basename>>>

    suspend fun updateData(
        session: Session,
        basename: Basename,
        availableDiff: DataDiffEntity?
    ): Either<DataFailure, Unit>

    suspend fun meta(
        session: Session,
        basename: Basename,
        updatedIds: List<Int>
    ): Either<DataFailure, Unit>
}