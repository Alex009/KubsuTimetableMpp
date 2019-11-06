package com.kubsu.timetable.domain.interactor.sync

import com.egroden.teaco.Either
import com.egroden.teaco.flatMap
import com.egroden.teaco.right
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.storage.user.session.Session
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway
import com.kubsu.timetable.extensions.def
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SyncMixinInteractorImpl(
    private val mixinGateway: SyncMixinGateway,
    private val userInfoGateway: UserInfoGateway
) : SyncMixinInteractor {
    override suspend fun registerDataDiff(entity: DataDiffEntity.Raw) = def {
        mixinGateway.registerDataDiff(entity)
    }

    override suspend fun syncData(): Either<DataFailure, Unit> = def {
        userInfoGateway
            .getCurrentSessionEitherFail()
            .flatMap { session ->
                mixinGateway
                    .checkMigrations(session, userInfoGateway.getCurrentTokenOrNull())
                    .flatMap {
                        val diffList = mixinGateway.getAvailableDiffList()
                        val rowDiffList = diffList.map(DataDiffEntity.Merged::raw)
                        handleAvailableDiffList(session, rowDiffList).flatMap {
                            mixinGateway
                                .diff(session)
                                .flatMap { (newTimestamp, basenameList) ->
                                    updateData(session, basenameList, diffList).flatMap {
                                        mixinGateway.delete(diffList)
                                        userInfoGateway.updateTimestamp(newTimestamp)
                                    }
                                }
                        }
                    }
            }
    }

    private suspend fun handleAvailableDiffList(
        session: Session,
        diffList: List<DataDiffEntity.Raw>
    ): Either<DataFailure, Unit> =
        diffList
            .map { mixinGateway.meta(session, it.basename, it.updatedIds) }
            .firstOrNull { it is Either.Left }
            ?: run {
                for (diff in diffList)
                    mixinGateway.deleteBasenameData(diff.basename, diff.deletedIds)
                mixinGateway.rawListHandled(diffList)
                Either.right(Unit)
            }

    private suspend fun updateData(
        session: Session,
        basenameList: List<Basename>,
        availableDiffList: List<DataDiffEntity.Merged>
    ): Either<DataFailure, Unit> =
        basenameList
            .map { basename ->
                mixinGateway.updateData(
                    session = session,
                    basename = basename,
                    availableDiff = availableDiffList.firstOrNull { it.basename == basename }
                )
            }
            .firstOrNull { it is Either.Left }
            ?: Either.right(Unit)

    override fun subscribeOnPushUpdates(): Flow<Either<DataFailure, Unit>> =
        mixinGateway
            .rowDataDiffListFlow()
            .map { diffList ->
                userInfoGateway
                    .getCurrentSessionEitherFail()
                    .flatMap {
                        handleAvailableDiffList(it, diffList)
                    }
            }
}