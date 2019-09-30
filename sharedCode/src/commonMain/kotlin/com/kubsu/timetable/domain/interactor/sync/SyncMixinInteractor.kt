package com.kubsu.timetable.domain.interactor.sync

interface SyncMixinInteractor {
    suspend fun updateData()
}