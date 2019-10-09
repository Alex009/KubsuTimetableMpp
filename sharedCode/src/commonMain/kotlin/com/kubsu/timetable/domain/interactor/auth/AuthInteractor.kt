package com.kubsu.timetable.domain.interactor.auth

import com.kubsu.timetable.*
import com.kubsu.timetable.domain.entity.UserEntity

interface AuthInteractor {
    suspend fun signIn(
        email: String,
        password: String
    ): Either<RequestFailure<List<SignInFail>>, UserEntity>

    suspend fun registrationUser(
        email: String,
        password: String
    ): Either<RequestFailure<List<UserInfoFail>>, Unit>

    suspend fun isUserAuthenticated(): Boolean

    suspend fun logout(): Either<DataFailure, Unit>
}
