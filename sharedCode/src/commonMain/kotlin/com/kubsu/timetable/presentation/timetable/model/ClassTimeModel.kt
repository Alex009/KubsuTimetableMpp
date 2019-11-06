package com.kubsu.timetable.presentation.timetable.model

import platform.SerializableModel
import platform.SerializeModel

@SerializeModel
data class ClassTimeModel(
    val id: Int,
    val number: Int,
    val start: String,
    val end: String
) : SerializableModel