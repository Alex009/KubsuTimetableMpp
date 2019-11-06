package com.kubsu.timetable.presentation.subscription.model

import platform.SerializableModel
import platform.SerializeModel

@SerializeModel
data class OccupationModel(
    val id: Int,
    val title: String,
    val code: String,
    val facultyId: Int
) : SerializableModel