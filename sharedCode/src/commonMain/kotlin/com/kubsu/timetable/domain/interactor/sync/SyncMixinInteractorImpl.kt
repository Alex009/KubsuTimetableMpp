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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class SyncMixinInteractorImpl(
    private val mixinGateway: SyncMixinGateway,
    private val userInfoGateway: UserInfoGateway
) : SyncMixinInteractor {
    override suspend fun registerDataDiff(entity: DataDiffEntity) = def {
        mixinGateway.registerDataDiff(entity)
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    override fun updateData(): Flow<Either<DataFailure, Unit>> =
        mixinGateway
            .getAvailableDiffListFlow(getUser = userInfoGateway::getCurrentUserOrNull)
            .map { diffList ->
                userInfoGateway
                    .getCurrentSessionEitherFail()
                    .flatMap { session ->
                        handleAvailableDiffList(session, diffList).flatMap {
                            mixinGateway
                                .diff(session)
                                .flatMap { (newTimestamp, basenameList) ->
                                    updateData(session, basenameList, newTimestamp, diffList)
                                }
                        }
                    }
            }
            .flowOn(Dispatchers.Default)

    private suspend fun handleAvailableDiffList(
        session: Session,
        diffList: List<DataDiffEntity>
    ): Either<DataFailure, Unit> = coroutineScope {
        diffList
            .map { mixinGateway.meta(session, it.basename, it.updatedIds) }
            .firstOrNull { it is Either.Left }
            ?: Either.right(mixinGateway.delete(diffList))
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
}