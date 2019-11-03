package com.kubsu.timetable.presentation.timetable.model

import com.kubsu.timetable.platform.SerializableModel
import com.kubsu.timetable.platform.SerializeModel

@SerializeModel
class UniversityInfoModel(
    val id: Int,
    val facultyId: Int,
    val typeOfWeek: TypeOfWeekModel,
    val weekNumber: Int
) : SerializableModel