package com.kubsu.timetable.data.network.dto.timetable.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UniversityInfoNetworkDto(
    @SerialName("faculty_id")
    val facultyId: Int,

    @SerialName("type_of_week")
    val typeOfWeek: Int
)