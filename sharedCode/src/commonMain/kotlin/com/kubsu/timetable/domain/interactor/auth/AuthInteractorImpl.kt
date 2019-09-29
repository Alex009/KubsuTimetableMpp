package com.kubsu.timetable.domain.interactor.auth

import com.kubsu.timetable.Either
import com.kubsu.timetable.def
import com.kubsu.timetable.AuthFail
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.WrapperFailure
import com.kubsu.timetable.domain.entity.UserEntity

class AuthInteractorImpl(
    private val gateway: AuthGateway
) : AuthInteractor {
    override suspend fun isUserAuthenticated(): Boolean = def {
        gateway.getUserOrNull() != null
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): Either<WrapperFailure<AuthFail>, Unit> = def {
        gateway.signIn(email, password)
    }

    override suspend fun registrationUser(
        email: String,
        password: String
    ): Either<WrapperFailure<AuthFail>, Unit> = def {
        gateway.registrationUser(email, password)
    }

    override suspend fun logout(): Either<NetworkFailure, Unit> = def {
        gateway.logout()
    }
}