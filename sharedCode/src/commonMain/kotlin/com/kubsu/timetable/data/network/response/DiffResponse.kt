package com.kubsu.timetable.data.network.response

import com.kubsu.timetable.domain.entity.Timestamp

class DiffResponse(
    val timestamp: Timestamp,
    val basenameList: List<String>
)