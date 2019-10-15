package com.kubsu.timetable.presentation.timetable.model

import platform.SerializableModel
import platform.SerializeModel

@SerializeModel
data class LecturerModel(
    val id: Int,
    val name: String,
    val surname: String,
    val patronymic: String
) : SerializableModel