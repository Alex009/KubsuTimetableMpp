package com.kubsu.timetable.domain.interactor.auth

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.UserInfoFail

interface AuthGateway {
    suspend fun signIn(
        email: String,
        password: String
    ): Either<RequestFailure<List<SignInFail>>, Unit>

    suspend fun registrationUser(
        email: String,
        password: String
    ): Either<RequestFailure<List<UserInfoFail>>, Unit>

    suspend fun logout(): Either<DataFailure, Unit>
}