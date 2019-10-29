package com.kubsu.timetable.domain.interactor.auth

import com.egroden.teaco.Either
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.domain.entity.UserEntity

interface AuthInteractor {
    suspend fun signInTransaction(
        email: String,
        password: String
    ): Either<RequestFailure<List<SignInFail>>, UserEntity>

    suspend fun registrationUser(
        email: String,
        password: String
    ): Either<RequestFailure<List<UserInfoFail>>, Unit>

    suspend fun logout()
}
