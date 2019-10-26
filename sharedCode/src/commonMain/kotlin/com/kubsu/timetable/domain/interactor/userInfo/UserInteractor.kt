package com.kubsu.timetable.domain.interactor.userInfo

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.data.storage.user.token.Token
import com.kubsu.timetable.data.storage.user.token.UndeliveredToken
import com.kubsu.timetable.domain.entity.UserEntity

interface UserInteractor {
    suspend fun getCurrentUserOrNull(): UserEntity?

    suspend fun newToken(token: UndeliveredToken): Either<DataFailure, Unit>

    suspend fun getCurrentTokenOrNull(): Token?

    suspend fun update(user: UserEntity): Either<RequestFailure<List<UserInfoFail>>, Unit>
}