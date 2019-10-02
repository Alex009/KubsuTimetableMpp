package com.kubsu.timetable.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class MainInfoNetworkDto(
    @SerialName("is_numerator")
    val isNumerator: Boolean
)