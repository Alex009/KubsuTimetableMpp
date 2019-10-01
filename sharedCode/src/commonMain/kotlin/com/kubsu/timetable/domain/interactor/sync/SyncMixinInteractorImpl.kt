package com.kubsu.timetable.domain.interactor.sync

import com.kubsu.timetable.*
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import com.kubsu.timetable.domain.interactor.main.MainGateway

class SyncMixinInteractorImpl(
    private val mixinGateway: SyncMixinGateway,
    private val mainGateway: MainGateway
) : SyncMixinInteractor {
    override suspend fun updateData(): Either<WrapperFailure<NoActiveUserFailure>, Unit> = def {
        val user = mainGateway.getCurrentUser()

        return@def if (user != null) {
            val diffList = mixinGateway.getAvailableDiffList(user.id)
            handleAvailableDiffList(diffList, user.id)

            val (newTimestamp, basenameList) = mixinGateway
                .diff(user.timestamp)
                .fold(
                    ifLeft = { return@def Either.left(WrapperFailure<NoActiveUserFailure>(it)) },
                    ifRight = { (timestamp, basenameList) -> timestamp to basenameList }
                )

            for (basename in basenameList)
                mixinGateway.updateData(
                    basename = basename,
                    availableDiff = diffList.first { it.basename == basename },
                    user = user
                )

            mainGateway.set(user.copy(timestamp = newTimestamp))

            Either.right(Unit)
        } else {
            Either.left(WrapperFailure(NoActiveUserFailure))
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