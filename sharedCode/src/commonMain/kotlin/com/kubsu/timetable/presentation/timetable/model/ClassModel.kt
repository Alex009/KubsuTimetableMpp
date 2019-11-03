package com.kubsu.timetable.presentation.timetable.model

import com.kubsu.timetable.platform.SerializableModel
import com.kubsu.timetable.platform.SerializeModel
import com.soywiz.klock.DayOfWeek

@SerializeModel
data class ClassModel(
    val id: Int,
    val title: String,
    val typeOfClass: TypeOfClassModel,
    val classroom: String,
    val classTime: ClassTimeModel,
    val day: DayOfWeek,
    val lecturer: LecturerModel,
    val timetableId: Int
) : SerializableModel