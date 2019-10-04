package com.kubsu.timetable.data.network.client.user

import com.kubsu.timetable.*
import com.kubsu.timetable.data.network.dto.UserNetworkDto

interface UserInfoNetworkClient {
    suspend fun registration(
        email: String,
        password: String
    ): Either<RequestFailure<Set<RegistrationFail>>, Unit>

    suspend fun signIn(
        email: String,
        password: String
    ): Either<RequestFailure<SignInFail>, UserNetworkDto>

    suspend fun update(user: UserNetworkDto): Either<NetworkFailure, Unit>
}