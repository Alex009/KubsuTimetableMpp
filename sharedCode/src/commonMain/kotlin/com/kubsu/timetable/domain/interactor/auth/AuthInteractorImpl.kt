package com.kubsu.timetable.domain.interactor.auth

import com.kubsu.timetable.*
import com.kubsu.timetable.domain.interactor.main.MainGateway

class AuthInteractorImpl(
    private val gateway: AuthGateway,
    private val mainGateway: MainGateway
) : AuthInteractor {
    override suspend fun isUserAuthenticated(): Boolean = def {
        mainGateway.getCurrentUser() != null
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