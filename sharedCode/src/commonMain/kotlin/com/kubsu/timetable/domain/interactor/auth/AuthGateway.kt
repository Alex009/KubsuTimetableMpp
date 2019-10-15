package com.kubsu.timetable.domain.interactor.auth

import com.kubsu.timetable.*
import com.kubsu.timetable.domain.entity.UserEntity

interface AuthGateway {
    suspend fun signIn(
        email: String,
        password: String
    ): Either<RequestFailure<List<SignInFail>>, Unit>

    suspend fun registrationUser(
        email: String,
        password: String
    ): Either<RequestFailure<List<RegistrationFail>>, Unit>

    suspend fun logout(user: UserEntity): Either<DataFailure, Unit>
}