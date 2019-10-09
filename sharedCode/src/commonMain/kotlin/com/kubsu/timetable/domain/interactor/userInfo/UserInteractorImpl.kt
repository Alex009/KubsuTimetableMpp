package com.kubsu.timetable.domain.interactor.userInfo

import com.kubsu.timetable.Either
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.UserUpdateFail
import com.kubsu.timetable.def
import com.kubsu.timetable.domain.entity.UserEntity

class UserInteractorImpl(
    private val gateway: UserInfoGateway
) : UserInteractor {
    override suspend fun getCurrentUserOrThrow(): UserEntity = def {
        requireNotNull(gateway.getCurrentUserOrNull())
    }

    override suspend fun update(
        user: UserEntity
    ): Either<RequestFailure<List<UserUpdateFail>>, Unit> = def {
        gateway.updateUserInfo(user)
    }
}