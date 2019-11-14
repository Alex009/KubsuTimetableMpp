package com.kubsu.timetable.domain.syncronizer

import com.egroden.teaco.mapLeft
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.storage.user.token.UndeliveredToken
import com.kubsu.timetable.domain.interactor.sync.SyncMixinInteractor
import com.kubsu.timetable.domain.interactor.userInfo.UserInteractor
import com.kubsu.timetable.platform.PlatformArgs
import com.kubsu.timetable.platform.whenNetworkConnectionBeActive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class InformationSynchronizerImpl(
    private val userInteractor: UserInteractor,
    private val syncMixinInteractor: SyncMixinInteractor,
    private val platformArgs: PlatformArgs
) : InformationSynchronizer {
    private var syncJob: Job? = null

    override fun awaitConnectionAndSync(
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
        syncJob = scope.launch(Dispatchers.Default) {
            updateToken(onFailure)

            // because at the first start there will be a fail
            // and you need to listen to the push
            syncMixinInteractor
                .syncData()
                .mapLeft(onFailure)

            subscribeOnPushUpdates(onFailure)
        }
    }

    private suspend fun updateToken(onFailure: (DataFailure) -> Unit) {
        (userInteractor.getCurrentTokenOrNull() as? UndeliveredToken)?.let {
            userInteractor.newToken(it).mapLeft(onFailure)
        }
    }

    private suspend fun subscribeOnPushUpdates(onFailure: (DataFailure) -> Unit) =
        syncMixinInteractor
            .subscribeOnPushUpdates()
            .collect { either ->
                either.mapLeft(onFailure)
            }

    private fun closeSync() {
        syncJob?.cancel()
        syncJob = null
    }
}