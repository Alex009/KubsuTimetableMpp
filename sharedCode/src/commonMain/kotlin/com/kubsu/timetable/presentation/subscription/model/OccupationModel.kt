package com.kubsu.timetable.presentation.subscription.model

import kotlinx.serialization.Serializable

@Serializable
data class OccupationModel(
    val id: Int,
    val title: String,
    val code: String,
    val facultyId: Int
)