package com.kubsu.timetable.data.network.client.update

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.network.dto.diff.DiffResponse
import com.kubsu.timetable.data.network.dto.diff.SyncResponse
import kotlinx.serialization.KSerializer

interface UpdateDataNetworkClient {
    suspend fun diff(): Either<DataFailure, DiffResponse>

    suspend fun sync(
        basename: String,
        existsIds: List<Int>
    ): Either<DataFailure, SyncResponse>

    suspend fun <T> meta(
        basename: String,
        basenameSerializer: KSerializer<T>,
        updatedIds: List<Int>
    ): Either<DataFailure, List<T>>
}