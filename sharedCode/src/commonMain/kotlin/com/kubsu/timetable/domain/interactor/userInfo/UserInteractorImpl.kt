package com.kubsu.timetable.domain.interactor.userInfo

import com.kubsu.timetable.Either
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.extensions.def

class UserInteractorImpl(
    private val gateway: UserInfoGateway
) : UserInteractor {
    override suspend fun getCurrentUserOrThrow(): UserEntity = def {
        requireNotNull(gateway.getCurrentUserOrNull())
    }

    override suspend fun update(
        user: UserEntity
    ): Either<RequestFailure<List<UserInfoFail>>, Unit> = def {
        gateway.updateUserInfo(user)
    }
}