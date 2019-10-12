package com.kubsu.timetable.domain.interactor.auth

import com.kubsu.timetable.*
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway

class AuthInteractorImpl(
    private val authGateway: AuthGateway,
    private val userInfoGateway: UserInfoGateway
) : AuthInteractor {
    override suspend fun signIn(
        email: String,
        password: String
    ): Either<RequestFailure<List<SignInFail>>, Unit> = def {
        authGateway.signIn(email, password)
    }

    override suspend fun registrationUser(
        email: String,
        password: String
    ): Either<RequestFailure<List<UserInfoFail>>, Unit> = def {
        authGateway.registrationUser(email, password)
    }


    override suspend fun isUserAuthenticated(): Boolean = def {
        userInfoGateway.getCurrentUserOrNull() != null
    }

    override suspend fun logout(): Either<DataFailure, Unit> = def {
        val user = userInfoGateway.getCurrentUserOrNull()
        if (user != null)
            authGateway.logout(user)
        else
            Either.left(DataFailure.NotAuthenticated("AuthInteractor#logout"))
    }
}