package com.kubsu.timetable.domain.interactor.auth

import com.egroden.teaco.Either
import com.egroden.teaco.map
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.interactor.appinfo.AppInfoGateway
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
    ): Either<RequestFailure<List<SignInFail>>, UserEntity> = def {
        val token = userInfoGateway.getCurrentTokenOrNull()
        authGateway
            .signInTransaction(
                email = email,
                password = password,
                token = token,
                withTransaction = {
                    appInfoGateway.checkAvailabilityOfUserInfo(
                        it.session,
                        it.user.id
                    )
                }
            )
    }

    override suspend fun registrationUser(
        email: String,
        password: String
    ): Either<RequestFailure<List<UserInfoFail>>, Unit> = def {
        authGateway.registrationUser(email, password)
    }

    override suspend fun logout() = def {
        userInfoGateway.getCurrentSessionEitherFail().map {
            authGateway.logout(it)
        }
        return@def
    }
}