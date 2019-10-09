package com.kubsu.timetable.domain.interactor.userInfo

import com.kubsu.timetable.Either
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.UserUpdateFail
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.UserEntity

interface UserInfoGateway {
    suspend fun getCurrentUserOrNull(): UserEntity?

    suspend fun updateUserInfo(user: UserEntity): Either<RequestFailure<List<UserUpdateFail>>, Unit>

    suspend fun updateTimestamp(user: UserEntity, timestamp: Timestamp)
}