package com.kubsu.timetable.presentation.subscription.model

import kotlinx.serialization.Serializable

@Serializable
data class FacultyModel(
    val id: Int,
    val title: String
)