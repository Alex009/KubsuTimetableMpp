package com.kubsu.timetable.domain.interactor.main

import com.kubsu.timetable.AuthFail
import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.UserEntity

interface UserInfoGateway {
    suspend fun registrationUser(
        email: String,
        password: String
    ): Either<RequestFailure<List<AuthFail>>, Unit>

    suspend fun getCurrentUserOrNull(): UserEntity?

    suspend fun updateUserInfo(user: UserEntity): Either<NetworkFailure, Unit>

    suspend fun updateTimestamp(user: UserEntity, timestamp: Timestamp)
}