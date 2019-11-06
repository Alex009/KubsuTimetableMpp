package com.kubsu.timetable.data.network.client.update

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.network.dto.diff.DiffResponse
import com.kubsu.timetable.data.network.dto.diff.MigrationResponse
import com.kubsu.timetable.data.network.dto.diff.SyncResponse
import com.kubsu.timetable.data.storage.user.session.Session
import com.kubsu.timetable.data.storage.user.token.Token
import kotlinx.serialization.KSerializer

interface UpdateDataNetworkClient {
    suspend fun checkMigrations(
        session: Session,
        token: Token?
    ): Either<DataFailure, List<MigrationResponse>>

    suspend fun diff(session: Session): Either<DataFailure, DiffResponse>

    suspend fun sync(
        session: Session,
        basename: String,
        existsIds: List<Int>
    ): Either<DataFailure, SyncResponse>

    suspend fun <T> meta(
        session: Session,
        basename: String,
        basenameSerializer: KSerializer<T>,
        updatedIds: List<Int>
    ): Either<DataFailure, List<T>>
}