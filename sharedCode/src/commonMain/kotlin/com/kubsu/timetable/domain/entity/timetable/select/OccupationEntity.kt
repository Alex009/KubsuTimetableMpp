package com.kubsu.timetable.domain.entity.timetable.select

data class OccupationEntity(
    val id: Int,
    val title: String,
    val code: String,
    val facultyId: Int
)