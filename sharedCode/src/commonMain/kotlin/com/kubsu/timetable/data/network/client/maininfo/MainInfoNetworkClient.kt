package com.kubsu.timetable.data.network.client.maininfo

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.network.dto.MainInfoNetworkDto

interface MainInfoNetworkClient {
    suspend fun selectMainInfo(): Either<NetworkFailure, MainInfoNetworkDto>
}