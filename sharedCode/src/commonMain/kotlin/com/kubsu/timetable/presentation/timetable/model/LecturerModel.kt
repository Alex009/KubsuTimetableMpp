package com.kubsu.timetable.presentation.timetable.model

import kotlinx.serialization.Serializable

@Serializable
data class LecturerModel(
    val id: Int,
    val name: String,
    val surname: String,
    val patronymic: String
)