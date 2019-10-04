package com.kubsu.timetable.domain.interactor.auth

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.domain.entity.UserEntity

interface AuthGateway {
    suspend fun signIn(
        email: String,
        password: String
    ): Either<RequestFailure<SignInFail>, UserEntity>

    suspend fun logout(): Either<NetworkFailure, Unit>
}