package com.kubsu.timetable.presentation.subscription.model

import platform.SerializableModel
import platform.SerializeModel

@SerializeModel
data class FacultyModel(
    val id: Int,
    val title: String
) : SerializableModel