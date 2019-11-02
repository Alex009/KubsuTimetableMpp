package com.kubsu.timetable.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.domain.syncronizer.InformationSynchronizer
import com.kubsu.timetable.firebase.CrashlyticsLogger

class SyncViewModel(
    informationSynchronizer: InformationSynchronizer
) : ViewModel() {
    init {
        informationSynchronizer.awaitConnectionAndSync(
            scope = viewModelScope,
            onFailure = ::onFailure
        )
    }

    private fun onFailure(failure: DataFailure) {
        if (failure !is DataFailure.NotAuthenticated)
            CrashlyticsLogger.logFailureToCrashlytics(
                failure = failure,
                tag = "InformationSynchronizer"
            )
    }

    class Factory(
        private val informationSynchronizer: InformationSynchronizer
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            SyncViewModel(informationSynchronizer) as T
    }
}