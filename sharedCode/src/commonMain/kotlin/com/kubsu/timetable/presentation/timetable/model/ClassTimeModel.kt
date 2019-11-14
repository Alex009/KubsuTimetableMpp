package com.kubsu.timetable.presentation.timetable.model

import com.kubsu.timetable.platform.SerializableModel
import com.kubsu.timetable.platform.SerializeModel

@SerializeModel
data class ClassTimeModel(
    val id: Int,
    val number: Int,
    val start: String,
    val end: String
) : SerializableModel