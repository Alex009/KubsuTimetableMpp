package com.kubsu.timetable.data.network.dto.timetable.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClassNetworkDto(
    val id: Int,
    val title: String,

    @SerialName("type_of_class")
    val typeOfClass: Int,

    val classroom: String,

    @SerialName("class_time_id")
    val classTimeId: Int,

    val weekday: Int,

    @SerialName("lecturer_id")
    val lecturerId: Int,

    @SerialName("timetable_id")
    val timetableId: Int
)