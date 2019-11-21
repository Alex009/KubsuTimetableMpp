package com.kubsu.timetable.presentation.timetable.model

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

@Parcelize
class UniversityInfoModel(
    val id: Int,
    val facultyId: Int,
    val typeOfWeek: TypeOfWeekModel,
    val weekNumber: Int
) : Parcelable