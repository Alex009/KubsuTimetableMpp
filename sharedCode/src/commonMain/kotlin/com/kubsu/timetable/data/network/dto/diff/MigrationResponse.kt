package com.kubsu.timetable.data.network.dto.diff

import kotlinx.serialization.Serializable

@Serializable
class MigrationResponse(
    val basename: String,
    val ids: List<Int>
)