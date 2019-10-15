package com.kubsu.timetable.presentation.timetable.model

import platform.SerializableModel
import platform.SerializeModel

@SerializeModel
class UniversityInfoModel(
    val facultyId: Int,
    val typeOfWeek: TypeOfWeekModel
) : SerializableModel