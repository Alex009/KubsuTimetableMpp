package com.kubsu.timetable.data.network.dto.timetable.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SubscriptionNetworkDto(
    val id: Int,

    val user: Int,

    val title: String,

    val subgroup: Int,

    @SerialName("is_main")
    val isMain: Boolean
)