package com.kubsu.timetable.data.network.dto.diff

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DataDiffNetworkDto(
    @SerialName("user_id")
    val userId: Int,

    @SerialName("message_title")
    val messageTitle: String,

    val basename: String,

    val ids: List<Int>
)