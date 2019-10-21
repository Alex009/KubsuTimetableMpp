package com.kubsu.timetable.presentation.timetable.model

import platform.SerializableModel
import platform.SerializeModel

@SerializeModel
class UniversityInfoModel(
    val id: Int,
    val facultyId: Int,
    val typeOfWeek: TypeOfWeekModel
) : SerializableModel