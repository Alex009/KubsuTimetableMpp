package com.kubsu.timetable.presentation.timetable.model

import kotlinx.serialization.Serializable

@Serializable
class UniversityInfoModel(
    val facultyId: Int,
    val typeOfWeek: TypeOfWeekModel
)