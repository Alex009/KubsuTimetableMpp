package com.kubsu.timetable.domain.interactor.main

import com.kubsu.timetable.*
import com.kubsu.timetable.domain.entity.UserEntity

class UserInteractorImpl(
    private val gateway: UserInfoGateway
) : UserInteractor {
    override suspend fun registrationUser(
        email: String,
        password: String
    ): Either<RequestFailure<List<AuthFail>>, Unit> = def {
        gateway.registrationUser(email, password)
    }

    override suspend fun getCurrentUserOrNull(): UserEntity? = def {
        gateway.getCurrentUserOrNull()
    }

    override suspend fun update(user: UserEntity): Either<NetworkFailure, Unit> = def {
        gateway.updateUserInfo(user)
    }
}