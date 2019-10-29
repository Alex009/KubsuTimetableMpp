package com.kubsu.timetable.data.network.client.user

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.data.network.dto.UserNetworkDto
import com.kubsu.timetable.data.storage.user.token.Token

interface UserInfoNetworkClient {
    suspend fun registration(
        email: String,
        password: String
    ): Either<RequestFailure<List<UserInfoFail>>, Unit>

    suspend fun signIn(
        email: String,
        password: String,
        token: Token?
    ): Either<RequestFailure<List<SignInFail>>, UserNetworkDto>

    suspend fun update(
        user: UserNetworkDto
    ): Either<RequestFailure<List<UserInfoFail>>, Unit>

    suspend fun updateToken(
        token: Token
    ): Either<DataFailure, Unit>

    suspend fun logout(): Either<DataFailure, Unit>
}