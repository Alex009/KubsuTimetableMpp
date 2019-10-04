package com.kubsu.timetable.domain.interactor.main

import com.kubsu.timetable.AuthFail
import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.domain.entity.UserEntity

interface UserInteractor {
    suspend fun registrationUser(
        email: String,
        password: String
    ): Either<RequestFailure<List<AuthFail>>, Unit>

    suspend fun getCurrentUserOrNull(): UserEntity?

    suspend fun update(user: UserEntity): Either<NetworkFailure, Unit>
}