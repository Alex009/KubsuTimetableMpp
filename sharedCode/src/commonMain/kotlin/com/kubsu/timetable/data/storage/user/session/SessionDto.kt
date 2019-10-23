package com.kubsu.timetable.data.storage.user.session

import com.kubsu.timetable.domain.entity.Timestamp

data class SessionDto(
    val id: String,
    val timestamp: Timestamp
)