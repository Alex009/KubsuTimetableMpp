package com.kubsu.timetable.data.network.client.update

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.Either
import com.kubsu.timetable.data.network.dto.response.DiffResponse
import com.kubsu.timetable.data.network.dto.response.SyncResponse

interface UpdateDataNetworkClient {
    suspend fun diff(timestamp: Long): Either<DataFailure, DiffResponse>

    suspend fun sync(
        basename: String,
        timestamp: Long,
        existsIds: List<Int>
    ): Either<DataFailure, SyncResponse>

    suspend fun <T> meta(basename: String, updatedIds: List<Int>): Either<DataFailure, List<T>>
}