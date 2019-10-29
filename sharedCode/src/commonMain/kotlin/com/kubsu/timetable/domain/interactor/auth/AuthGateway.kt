package com.kubsu.timetable.domain.interactor.auth

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.data.storage.user.token.Token
import com.kubsu.timetable.domain.entity.UserEntity

interface AuthGateway {
    suspend fun signInTransaction(
        email: String,
        password: String,
        token: Token?,
        withTransaction: suspend (UserEntity) -> Either<DataFailure, Unit>
    ): Either<RequestFailure<List<SignInFail>>, UserEntity>

    suspend fun registrationUser(
        email: String,
        password: String
    ): Either<RequestFailure<List<UserInfoFail>>, Unit>

    suspend fun logout()
}