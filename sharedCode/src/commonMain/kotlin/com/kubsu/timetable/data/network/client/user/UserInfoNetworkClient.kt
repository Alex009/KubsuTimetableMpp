package com.kubsu.timetable.data.network.client.user

import com.kubsu.timetable.*
import com.kubsu.timetable.data.network.dto.UserNetworkDto

interface UserInfoNetworkClient {
    suspend fun registration(
        email: String,
        password: String
    ): Either<RequestFailure<List<UserInfoFail>>, Unit>

    suspend fun signIn(
        email: String,
        password: String
    ): Either<RequestFailure<List<SignInFail>>, UserNetworkDto>

    suspend fun update(user: UserNetworkDto): Either<RequestFailure<List<UserUpdateFail>>, Unit>

    suspend fun logout(user: UserNetworkDto): Either<DataFailure, Unit>
}