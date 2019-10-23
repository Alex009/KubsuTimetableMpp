package com.kubsu.timetable.domain.interactor.userInfo

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.data.storage.user.token.TokenDto
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.UserEntity

interface UserInfoGateway {
    suspend fun getCurrentUserOrNull(): UserEntity?

    suspend fun updateUserInfo(user: UserEntity): Either<RequestFailure<List<UserInfoFail>>, Unit>

    suspend fun updateToken(token: String): Either<DataFailure, Unit>

    suspend fun getCurrentTokenOrNull(): TokenDto?

    suspend fun updateTimestamp(timestamp: Timestamp): Either<DataFailure, Unit>
}