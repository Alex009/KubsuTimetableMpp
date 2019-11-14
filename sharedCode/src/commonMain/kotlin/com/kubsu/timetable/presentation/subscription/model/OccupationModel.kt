package com.kubsu.timetable.presentation.subscription.model

import com.kubsu.timetable.platform.SerializableModel
import com.kubsu.timetable.platform.SerializeModel

@SerializeModel
data class OccupationModel(
    val id: Int,
    val title: String,
    val code: String,
    val facultyId: Int
) : SerializableModel