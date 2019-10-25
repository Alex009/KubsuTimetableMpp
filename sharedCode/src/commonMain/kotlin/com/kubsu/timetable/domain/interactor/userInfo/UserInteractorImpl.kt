package com.kubsu.timetable.domain.interactor.userInfo

import com.egroden.teaco.Either
import com.egroden.teaco.right
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.extensions.def

class UserInteractorImpl(
    private val gateway: UserInfoGateway
) : UserInteractor {
    override suspend fun getCurrentUserOrNull(): UserEntity? = def {
        gateway.getCurrentUserOrNull()
    }

    override suspend fun newToken(token: String): Either<DataFailure, Unit> = def {
        gateway.updateToken(token)
    }

    override suspend fun updateToken(): Either<DataFailure, Unit> = def {
        val token = gateway.getCurrentTokenOrNull()
        if (token != null && !token.delivered)
            gateway.updateToken(token.value)
        else
            Either.right(Unit)
    }

    override suspend fun update(
        user: UserEntity
    ): Either<RequestFailure<List<UserInfoFail>>, Unit> = def {
        gateway.updateUserInfo(user)
    }
}