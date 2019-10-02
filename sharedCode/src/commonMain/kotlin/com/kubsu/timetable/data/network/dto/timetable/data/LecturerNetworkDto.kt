package com.kubsu.timetable.data.network.dto.timetable.data

import kotlinx.serialization.Serializable

@Serializable
class LecturerNetworkDto(
    val id: Int,
    val name: String,
    val surname: String,
    val patronymic: String
)