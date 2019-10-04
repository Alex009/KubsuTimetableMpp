package com.kubsu.timetable.domain.interactor.sync

import com.kubsu.timetable.*
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import com.kubsu.timetable.domain.interactor.main.UserInfoGateway

class SyncMixinInteractorImpl(
    private val mixinGateway: SyncMixinGateway,
    private val userInfoGateway: UserInfoGateway
) : SyncMixinInteractor {
    override suspend fun updateData(): Either<RequestFailure<NoActiveUserFailure>, Unit> = def {
        val user = userInfoGateway.getCurrentUserOrNull()

        return@def if (user != null) {
            val diffList = mixinGateway.getAvailableDiffList(user.id)
            handleAvailableDiffList(diffList, user.id)

            val (newTimestamp, basenameList) = mixinGateway
                .diff(user.timestamp)
                .fold(
                    ifLeft = { return@def RequestFailure.eitherLeft(it) },
                    ifRight = { (timestamp, basenameList) -> timestamp to basenameList }
                )

            for (basename in basenameList)
                mixinGateway.updateData(
                    basename = basename,
                    availableDiff = diffList.first { it.basename == basename },
                    user = user
                )

            userInfoGateway.updateTimestamp(user, newTimestamp)

            Either.right(Unit)
        } else {
            RequestFailure.eitherLeft(NoActiveUserFailure)
        }
    }

    private suspend fun handleAvailableDiffList(
        diffList: List<DataDiffEntity>,
        userId: Int
    ): Either<NetworkFailure, Unit> {
        for (diff in diffList)
            mixinGateway
                .meta(diff.basename, userId, diff.updatedIds)
                .mapLeft { return Either.left(it) }

        mixinGateway.delete(diffList)
        return Either.right(Unit)
    }
}