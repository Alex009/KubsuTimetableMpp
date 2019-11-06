package com.kubsu.timetable.domain.interactor.sync

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.storage.user.session.Session
import com.kubsu.timetable.data.storage.user.token.Token
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import kotlinx.coroutines.flow.Flow

interface SyncMixinGateway {
    suspend fun registerDataDiff(entity: DataDiffEntity.Raw)

    suspend fun checkMigrations(session: Session, token: Token?): Either<DataFailure, Unit>
    suspend fun getAvailableDiffList(): List<DataDiffEntity.Merged>

    fun rowDataDiffListFlow(): Flow<List<DataDiffEntity.Raw>>

    suspend fun deleteBasenameData(basename: Basename, deletedIds: List<Int>)
    suspend fun delete(list: List<DataDiffEntity.Merged>)

    suspend fun rawListHandled(list: List<DataDiffEntity.Raw>)

    suspend fun diff(session: Session): Either<DataFailure, Pair<Timestamp, List<Basename>>>

    suspend fun updateData(
        session: Session,
        basename: Basename,
        availableDiff: DataDiffEntity.Merged?
    ): Either<DataFailure, Unit>

    suspend fun meta(
        session: Session,
        basename: Basename,
        updatedIds: List<Int>
    ): Either<DataFailure, Unit>
}