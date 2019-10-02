package com.kubsu.timetable.data.network.dto.timetable.data

import kotlinx.serialization.Serializable

@Serializable
class ClassTimeNetworkDto(
    val id: Int,
    val number: Int,
    val start: String,
    val end: String
)