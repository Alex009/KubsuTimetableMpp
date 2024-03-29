package com.kubsu.timetable.data.network.dto.diff

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DiffResponse(
    val timestamp: Long,

    @SerialName("base_names")
    val basenameList: List<String>
)