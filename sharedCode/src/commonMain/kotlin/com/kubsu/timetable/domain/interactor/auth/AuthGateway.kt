package com.kubsu.timetable.domain.interactor.auth

import com.kubsu.timetable.AuthFail
import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.WrapperFailure

interface AuthGateway {
    suspend fun signIn(email: String, password: String): Either<WrapperFailure<AuthFail>, Unit>

    suspend fun registrationUser(email: String, password: String): Either<WrapperFailure<AuthFail>, Unit>

    suspend fun logout(): Either<NetworkFailure, Unit>
}