package com.kubsu.timetable.presentation.timetable.model

import com.soywiz.klock.DayOfWeek
import platform.SerializableModel
import platform.SerializeModel

@SerializeModel
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
) : SerializableModel