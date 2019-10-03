package com.kubsu.timetable.data.network.client.maininfo

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.network.dto.MainInfoNetworkDto

class MainInfoNetworkClientImpl : MainInfoNetworkClient {
    override suspend fun selectMainInfo(): Either<NetworkFailure, MainInfoNetworkDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}