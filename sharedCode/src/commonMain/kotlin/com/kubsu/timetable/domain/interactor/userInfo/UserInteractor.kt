package com.kubsu.timetable.domain.interactor.userInfo

import com.egroden.teaco.Either
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.domain.entity.UserEntity

interface UserInteractor {
    suspend fun update(user: UserEntity): Either<RequestFailure<List<UserInfoFail>>, Unit>
}