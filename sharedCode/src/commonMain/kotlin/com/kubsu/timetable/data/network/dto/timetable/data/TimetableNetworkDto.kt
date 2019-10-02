package com.kubsu.timetable.data.network.dto.timetable.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TimetableNetworkDto(
    val id: Int,

    @SerialName("type_of_week")
    val typeOfWeek: Int,

    @SerialName("subgroup_id")
    val subgroupId: Int
)