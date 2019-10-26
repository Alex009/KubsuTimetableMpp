package com.kubsu.timetable.domain.interactor.sync

import com.egroden.teaco.Either
import com.egroden.teaco.flatMap
import com.egroden.teaco.right
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway
import com.kubsu.timetable.extensions.def
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

    override fun updateData(): Flow<Either<DataFailure, Unit>> =
        mixinGateway
            .getAvailableDiffListFlowForCurrentUser()
            .map { diffList ->
                handleAvailableDiffList(diffList).flatMap {
                    mixinGateway
                        .diff()
                        .flatMap { (newTimestamp, basenameList) ->
                            handleBasenameList(basenameList, newTimestamp, diffList)
                        }
                }
            }

    private suspend fun handleAvailableDiffList(
        diffList: List<DataDiffEntity>
    ): Either<DataFailure, Unit> = coroutineScope {
        diffList
            .map { async { mixinGateway.meta(it.basename, it.updatedIds) } }
            .awaitAll()
            .firstOrNull { it is Either.Left }
            ?: Either.right(mixinGateway.delete(diffList))
    }

    private suspend fun handleBasenameList(
        basenameList: List<Basename>,
        timestamp: Timestamp,
        diffList: List<DataDiffEntity>
    ): Either<DataFailure, Unit> = coroutineScope {
        basenameList
            .map { basename ->
                async {
                    mixinGateway.updateData(
                        basename = basename,
                        availableDiff = diffList.firstOrNull { it.basename == basename }
                    )
                }
            }
            .awaitAll()
            .firstOrNull { it is Either.Left }
            ?: userInfoGateway.updateTimestamp(timestamp)
    }
}