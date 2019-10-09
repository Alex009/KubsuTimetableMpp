package com.kubsu.timetable.domain.interactor.userInfo

import com.kubsu.timetable.Either
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.UserUpdateFail
import com.kubsu.timetable.domain.entity.UserEntity

interface UserInteractor {
    suspend fun getCurrentUserOrThrow(): UserEntity

    suspend fun update(user: UserEntity): Either<RequestFailure<List<UserUpdateFail>>, Unit>
}