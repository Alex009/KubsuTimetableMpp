package com.kubsu.timetable.presentation.subscription.model

import com.kubsu.timetable.platform.SerializableModel
import com.kubsu.timetable.platform.SerializeModel

@SerializeModel
data class FacultyModel(
    val id: Int,
    val title: String
) : SerializableModel