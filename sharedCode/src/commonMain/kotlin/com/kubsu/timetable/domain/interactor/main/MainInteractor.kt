package com.kubsu.timetable.domain.interactor.main

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.domain.entity.MainInfoEntity
import com.kubsu.timetable.domain.entity.UserEntity

interface MainInteractor {
    suspend fun getMainInfo(): Either<NetworkFailure, MainInfoEntity>

    suspend fun getCurrentUser(): UserEntity?
}