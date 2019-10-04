package com.kubsu.timetable.domain.interactor.auth

import com.kubsu.timetable.*
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.interactor.main.UserInfoGateway

class AuthInteractorImpl(
    private val authGateway: AuthGateway,
    private val userInfoGateway: UserInfoGateway
) : AuthInteractor {
    override suspend fun signIn(
        email: String,
        password: String
    ): Either<RequestFailure<List<AuthFail>>, UserEntity> = def {
        authGateway.signIn(email, password)
    }

    override suspend fun isUserAuthenticated(): Boolean = def {
        userInfoGateway.getCurrentUserOrNull() != null
    }

    override suspend fun logout(): Either<NetworkFailure, Unit> = def {
        authGateway.logout()
    }
}