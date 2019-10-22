package com.kubsu.timetable.data.network.client.update

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.network.dto.UserNetworkDto
import com.kubsu.timetable.data.network.dto.response.DiffResponse
import com.kubsu.timetable.data.network.dto.response.SyncResponse

interface UpdateDataNetworkClient {
    suspend fun diff(user: UserNetworkDto, timestamp: Long): Either<DataFailure, DiffResponse>

    suspend fun sync(
        user: UserNetworkDto,
        basename: String,
        timestamp: Long,
        existsIds: List<Int>
    ): Either<DataFailure, SyncResponse>

    suspend fun <T> meta(
        user: UserNetworkDto,
        basename: String,
        updatedIds: List<Int>
    ): Either<DataFailure, List<T>>
}