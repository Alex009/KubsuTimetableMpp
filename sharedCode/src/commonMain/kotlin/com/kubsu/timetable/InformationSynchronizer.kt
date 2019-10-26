package com.kubsu.timetable

import com.egroden.teaco.mapLeft
import com.kubsu.timetable.data.storage.user.token.UndeliveredToken
import com.kubsu.timetable.domain.interactor.sync.SyncMixinInteractor
import com.kubsu.timetable.domain.interactor.userInfo.UserInteractor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import platform.PlatformArgs
import platform.whenNetworkConnectionBeActive

class InformationSynchronizer(
    private val userInteractor: UserInteractor,
    private val syncMixinInteractor: SyncMixinInteractor,
    private val platformArgs: PlatformArgs,
    private val onFailure: (DataFailure) -> Unit
) {
    private var syncJob: Job? = null

    fun awaitConnectionAndSync() {
        whenNetworkConnectionBeActive(
            platformArgs = platformArgs,
            onActive = ::startSynchronization,
            onInactive = ::closeSync
        )
    }

    private fun startSynchronization() {
        syncJob?.cancel()
        syncJob = GlobalScope.launch {
            updateToken()
            syncData()
        }
    }

    private suspend fun updateToken() {
        val token = userInteractor.getCurrentTokenOrNull()
        if (token is UndeliveredToken)
            userInteractor
                .newToken(token)
                .mapLeft(onFailure)
    }

    private suspend fun syncData() =
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