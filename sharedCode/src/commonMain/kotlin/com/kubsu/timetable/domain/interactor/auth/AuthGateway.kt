package com.kubsu.timetable.domain.interactor.auth

import com.kubsu.timetable.Either
import com.kubsu.timetable.AuthFail
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.WrapperFailure
import com.kubsu.timetable.domain.entity.UserEntity

interface AuthGateway {
    suspend fun getUserOrNull(): UserEntity?

    suspend fun signIn(email: String, password: String): Either<WrapperFailure<AuthFail>, Unit>

    suspend fun registrationUser(email: String, password: String): Either<WrapperFailure<AuthFail>, Unit>

    suspend fun logout(): Either<NetworkFailure, Unit>
}