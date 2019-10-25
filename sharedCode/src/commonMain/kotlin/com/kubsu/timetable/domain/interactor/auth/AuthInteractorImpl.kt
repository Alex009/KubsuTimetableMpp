package com.kubsu.timetable.domain.interactor.auth

import com.egroden.teaco.Either
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway
import com.kubsu.timetable.extensions.def

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

    override suspend fun logout() = def {
        authGateway.logout()
    }
}