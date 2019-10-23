package com.kubsu.timetable.data.network.client.update

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.network.dto.response.DiffResponse
import com.kubsu.timetable.data.network.dto.response.SyncResponse
import com.kubsu.timetable.data.storage.user.session.SessionDto

interface UpdateDataNetworkClient {
    suspend fun diff(session: SessionDto): Either<DataFailure, DiffResponse>

    suspend fun sync(
        session: SessionDto,
        basename: String,
        existsIds: List<Int>
    ): Either<DataFailure, SyncResponse>

    suspend fun <T> meta(
        session: SessionDto,
        basename: String,
        updatedIds: List<Int>
    ): Either<DataFailure, List<T>>
}