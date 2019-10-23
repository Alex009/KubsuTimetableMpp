package com.kubsu.timetable.domain.interactor.userInfo

import com.egroden.teaco.Either
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

    override suspend fun updateToken(token: String): Either<DataFailure, Unit> = def {
        gateway.updateToken(token)
    }

    override suspend fun update(
        user: UserEntity
    ): Either<RequestFailure<List<UserInfoFail>>, Unit> = def {
        gateway.updateUserInfo(user)
    }
}