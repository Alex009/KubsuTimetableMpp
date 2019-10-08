package com.kubsu.timetable.domain.interactor.sync

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.Either
import com.kubsu.timetable.def
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway
import com.kubsu.timetable.flatMap
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SyncMixinInteractorImpl(
    private val mixinGateway: SyncMixinGateway,
    private val userInfoGateway: UserInfoGateway
) : SyncMixinInteractor {
    override suspend fun updateData(): Either<DataFailure, Unit> = def {
        val user = userInfoGateway.getCurrentUserOrNull()

        return@def if (user != null) {
            val diffList = mixinGateway.getAvailableDiffList(user.id)
            handleAvailableDiffList(diffList, user.id).flatMap {
                mixinGateway
                    .diff(user.timestamp)
                    .flatMap { (newTimestamp, basenameList) ->
                        handleBasenameList(basenameList, newTimestamp, diffList, user)
                    }
            }
        } else {
            Either.left(DataFailure.NotAuthenticated)
        }
    }

    private suspend fun handleAvailableDiffList(
        diffList: List<DataDiffEntity>,
        userId: Int
    ): Either<DataFailure, Unit> = coroutineScope {
        diffList
            .map { async { mixinGateway.meta(it.basename, userId, it.updatedIds) } }
            .map { it.await() }
            .firstOrNull { it is Either.Left }
            ?: Either.right(mixinGateway.delete(diffList))
    }

    private suspend fun handleBasenameList(
        basenameList: List<Basename>,
        timestamp: Timestamp,
        diffList: List<DataDiffEntity>,
        user: UserEntity
    ): Either<DataFailure, Unit> = coroutineScope {
        basenameList
            .map { basename ->
                async {
                    mixinGateway.updateData(
                        basename = basename,
                        availableDiff = diffList.first { it.basename == basename },
                        user = user
                    )
                }
            }
            .map { it.await() }
            .firstOrNull { it is Either.Left }
            ?: Either.right(userInfoGateway.updateTimestamp(user, timestamp))
    }
}