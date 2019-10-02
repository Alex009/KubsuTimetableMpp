package com.kubsu.timetable.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DiffResponse(
    val timestamp: Long,

    @SerialName("basename_list")
    val basenameList: List<String>
)