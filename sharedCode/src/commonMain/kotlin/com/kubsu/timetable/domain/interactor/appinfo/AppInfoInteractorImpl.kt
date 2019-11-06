package com.kubsu.timetable.domain.interactor.appinfo

import com.egroden.teaco.Either
import com.egroden.teaco.flatMap
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway
import com.kubsu.timetable.extensions.def

class AppInfoInteractorImpl(
    private val appInfoGateway: AppInfoGateway,
    private val userInfoGateway: UserInfoGateway
) : AppInfoInteractor {
    override suspend fun invalidate(): Either<DataFailure, Unit> = def {
        userInfoGateway.getCurrentUserEitherFailure().flatMap { user ->
            userInfoGateway.getCurrentSessionEitherFail().flatMap { session ->
                appInfoGateway.clearUserInfo(userId = user.id)
                appInfoGateway.updateInfo(session, userId = user.id)
            }
        }
    }
}