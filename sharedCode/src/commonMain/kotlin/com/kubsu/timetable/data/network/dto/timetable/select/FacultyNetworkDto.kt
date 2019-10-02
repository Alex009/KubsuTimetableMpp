package com.kubsu.timetable.data.network.dto.timetable.select

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class FacultyNetworkDto(
    val id: Int,
    val title: String,

    @SerialName("short_title")
    val shortTitle: String
)