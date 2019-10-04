package com.kubsu.timetable.data.network.client.user

import com.kubsu.timetable.AuthFail
import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.data.network.dto.UserNetworkDto

interface UserInfoNetworkClient {
    suspend fun registration(
        email: String,
        password: String
    ): Either<RequestFailure<List<AuthFail>>, Unit>

    suspend fun signIn(
        email: String,
        password: String
    ): Either<RequestFailure<List<AuthFail>>, UserNetworkDto>

    suspend fun update(user: UserNetworkDto): Either<NetworkFailure, Unit>
}