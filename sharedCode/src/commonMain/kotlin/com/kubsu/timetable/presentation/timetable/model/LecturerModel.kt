package com.kubsu.timetable.presentation.timetable.model

import com.kubsu.timetable.platform.SerializableModel
import com.kubsu.timetable.platform.SerializeModel

@SerializeModel
data class LecturerModel(
    val id: Int,
    val name: String,
    val surname: String,
    val patronymic: String
) : SerializableModel