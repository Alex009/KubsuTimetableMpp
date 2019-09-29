package com.kubsu.timetable.data.network.response

import com.kubsu.timetable.domain.entity.Timestamp

class SyncResponse(
    val updatedIds: List<Int>,
    val deletedIds: List<Int>,
    val timestamp: Timestamp
)