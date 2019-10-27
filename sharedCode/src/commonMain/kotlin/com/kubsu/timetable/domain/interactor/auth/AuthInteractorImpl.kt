package com.kubsu.timetable.domain.interactor.auth

import com.egroden.teaco.Either
import com.egroden.teaco.flatMap
import com.egroden.teaco.mapLeft
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.domain.interactor.timetable.AppInfoGateway
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway
import com.kubsu.timetable.extensions.def

class AuthInteractorImpl(
    private val authGateway: AuthGateway,
    private val userInfoGateway: UserInfoGateway,
    private val appInfoGateway: AppInfoGateway
) : AuthInteractor {
    override suspend fun signInTransaction(
        email: String,
        password: String
    ): Either<RequestFailure<List<SignInFail>>, Unit> = def {
        val token = userInfoGateway.getCurrentTokenOrNull()
        authGateway
            .signIn(email, password, token)
            .flatMap { user ->
                appInfoGateway
                    .updateInfo(user.id)
                    .mapLeft {
                        // Cancel transaction
                        authGateway.logout() // drop nested failure
                        RequestFailure<List<SignInFail>>(it)
                    }
            }
    }

    override suspend fun registrationUser(
        email: String,
        password: String
    ): Either<RequestFailure<List<UserInfoFail>>, Unit> = def {
        authGateway.registrationUser(email, password)
    }

    override suspend fun logout() = def {
        authGateway.logout()
    }
}