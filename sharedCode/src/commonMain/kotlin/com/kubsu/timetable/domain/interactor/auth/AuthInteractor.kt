package com.kubsu.timetable.domain.interactor.auth

import com.kubsu.timetable.*

interface AuthInteractor {
    suspend fun signIn(
        email: String,
        password: String
    ): Either<RequestFailure<List<SignInFail>>, Unit>

    suspend fun registrationUser(
        email: String,
        password: String
    ): Either<RequestFailure<List<UserInfoFail>>, Unit>

    suspend fun isUserAuthenticated(): Boolean

    suspend fun logout(): Either<DataFailure, Unit>
}
