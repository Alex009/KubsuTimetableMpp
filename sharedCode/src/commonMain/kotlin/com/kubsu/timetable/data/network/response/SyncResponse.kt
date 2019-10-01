package com.kubsu.timetable.data.network.response

data class SyncResponse(
    val updatedIds: List<Int>,
    val deletedIds: List<Int>
)