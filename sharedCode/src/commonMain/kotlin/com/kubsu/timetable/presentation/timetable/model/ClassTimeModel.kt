package com.kubsu.timetable.presentation.timetable.model

import kotlinx.serialization.Serializable

@Serializable
data class ClassTimeModel(
    val id: Int,
    val number: Int,
    val start: String,
    val end: String
)