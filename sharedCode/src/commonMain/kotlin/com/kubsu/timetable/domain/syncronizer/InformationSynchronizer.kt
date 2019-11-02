package com.kubsu.timetable.domain.syncronizer

import com.kubsu.timetable.DataFailure
import kotlinx.coroutines.CoroutineScope

interface InformationSynchronizer {
    fun awaitConnectionAndSync(
        scope: CoroutineScope,
        onFailure: (DataFailure) -> Unit
    )
}