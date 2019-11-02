package com.kubsu.timetable.domain.interactor.userInfo

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.data.storage.user.session.Session
import com.kubsu.timetable.data.storage.user.token.Token
import com.kubsu.timetable.data.storage.user.token.UndeliveredToken
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.UserEntity

interface UserInfoGateway {
    fun getCurrentUserOrNull(): UserEntity?

    suspend fun updateUserInfo(
        session: Session,
        user: UserEntity
    ): Either<RequestFailure<List<UserInfoFail>>, Unit>

    suspend fun updateToken(token: UndeliveredToken): Either<DataFailure, Unit>

    fun getCurrentToken(): Token

    fun getCurrentSessionEitherFail(): Either<DataFailure.NotAuthenticated, Session>

    fun updateTimestamp(timestamp: Timestamp): Either<DataFailure, Unit>
}