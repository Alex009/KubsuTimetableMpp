package com.kubsu.timetable.domain.interactor.sync

import com.egroden.teaco.Either
import com.egroden.teaco.flatMap
import com.egroden.teaco.right
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.storage.user.session.Session
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway
import com.kubsu.timetable.extensions.def
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SyncMixinInteractorImpl(
    private val mixinGateway: SyncMixinGateway,
    private val userInfoGateway: UserInfoGateway
) : SyncMixinInteractor {
    override suspend fun registerDataDiff(entity: DataDiffEntity) = def {
        mixinGateway.registerDataDiff(entity)
    }

    override suspend fun updateData(): Flow<Either<DataFailure, Unit>> = def {
        userInfoGateway
            .getCurrentSessionEitherFail()
            .flatMap { session ->
                mixinGateway
                    .diff(session)
                    .flatMap { (newTimestamp, basenameList) ->
                        val diffList = mixinGateway.getAvailableDiffList()
                        updateData(session, basenameList, newTimestamp, diffList)
                    }
            }

        return@def subscribeOnPushUpdates()
    }

    private suspend fun updateData(
        session: Session,
        basenameList: List<Basename>,
        timestamp: Timestamp,
        diffList: List<DataDiffEntity>
    ): Either<DataFailure, Unit> = coroutineScope {
        basenameList
            .map { basename ->
                mixinGateway.updateData(
                    session = session,
                    basename = basename,
                    availableDiff = diffList.firstOrNull { it.basename == basename }
                )
            }
            .firstOrNull { it is Either.Left }
            ?: userInfoGateway.updateTimestamp(timestamp)
    }

    private fun subscribeOnPushUpdates(): Flow<Either<DataFailure, Unit>> =
        mixinGateway
            .dataDiffListFlow()
            .map { diffList ->
                userInfoGateway
                    .getCurrentSessionEitherFail()
                    .flatMap {
                        handleAvailableDiffList(it, diffList)
                    }
            }

    private suspend fun handleAvailableDiffList(
        session: Session,
        diffList: List<DataDiffEntity>
    ): Either<DataFailure, Unit> = coroutineScope {
        diffList
            .map { mixinGateway.meta(session, it.basename, it.updatedIds) }
            .firstOrNull { it is Either.Left }
            ?: Either.right(mixinGateway.delete(diffList))
    }
}