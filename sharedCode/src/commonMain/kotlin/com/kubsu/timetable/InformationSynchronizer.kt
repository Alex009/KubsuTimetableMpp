package com.kubsu.timetable

import com.egroden.teaco.mapLeft
import com.kubsu.timetable.data.storage.user.token.UndeliveredToken
import com.kubsu.timetable.domain.interactor.sync.SyncMixinInteractor
import com.kubsu.timetable.domain.interactor.userInfo.UserInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import platform.PlatformArgs
import platform.whenNetworkConnectionBeActive

class InformationSynchronizer(
    private val userInteractor: UserInteractor,
    private val syncMixinInteractor: SyncMixinInteractor,
    private val platformArgs: PlatformArgs
) {
    private var syncJob: Job? = null

    fun awaitConnectionAndSync(
        scope: CoroutineScope,
        onFailure: (DataFailure) -> Unit
    ) =
        whenNetworkConnectionBeActive(
            platformArgs = platformArgs,
            onActive = { startSynchronization(scope, onFailure) },
            onInactive = ::closeSync
        )

    private fun startSynchronization(
        scope: CoroutineScope,
        onFailure: (DataFailure) -> Unit
    ) {
        syncJob?.cancel()
        syncJob = scope.launch {
            updateToken(onFailure)
            syncData(onFailure)
        }
    }

    private suspend fun updateToken(onFailure: (DataFailure) -> Unit) {
        (userInteractor.getCurrentTokenOrNull() as? UndeliveredToken)?.let {
            userInteractor.newToken(it).mapLeft(onFailure)
        }
    }

    private suspend fun syncData(onFailure: (DataFailure) -> Unit) =
        syncMixinInteractor
            .updateData()
            .collect { either ->
                either.mapLeft(onFailure)
            }

    private fun closeSync() {
        syncJob?.cancel()
        syncJob = null
    }
}