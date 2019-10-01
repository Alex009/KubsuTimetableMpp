package com.kubsu.timetable.domain.interactor.main

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.def
import com.kubsu.timetable.domain.entity.MainInfoEntity
import com.kubsu.timetable.domain.entity.UserEntity

class MainInteractorImpl(
    private val gateway: MainGateway
) : MainInteractor {
    override suspend fun getMainInfo(): Either<NetworkFailure, MainInfoEntity> = def {
        gateway.getMainInfo()
    }

    override suspend fun getCurrentUser(): UserEntity? = def {
        gateway.getCurrentUser()
    }
}