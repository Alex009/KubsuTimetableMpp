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

    @SerialName("silent_ids")
    val silentIds: List<Int>,

    @SerialName("noisy_ids")
    val noisyIds: List<Int>
)