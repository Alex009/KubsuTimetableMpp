package com.kubsu.timetable.presentation.timetable.model

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize
import com.soywiz.klock.DayOfWeek

@Parcelize
data class ClassModel(
    val id: Int,
    val title: String,
    val typeOfClass: TypeOfClassModel,
    val classroom: String,
    val classTime: ClassTimeModel,
    val day: DayOfWeek,
    val lecturer: LecturerModel,
    val timetableId: Int,
    val needToEmphasize: Boolean
) : Parcelable