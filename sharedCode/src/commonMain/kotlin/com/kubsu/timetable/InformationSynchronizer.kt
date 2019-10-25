package com.kubsu.timetable

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
    private val platformArgs: PlatformArgs
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
            userInteractor.updateToken()
            syncMixinInteractor
                .updateData()
                .collect()
        }
    }

    private fun closeSync() {
        syncJob?.cancel()
        syncJob = null
    }
}