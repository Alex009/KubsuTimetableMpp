package com.kubsu.timetable.data.network.dto.timetable.select

import kotlinx.serialization.Serializable

@Serializable
class OccupationNetworkDto(
    val id: Int,
    val title: String,
    val code: String
)