package com.kubsu.timetable.domain.entity.timetable.data

import com.soywiz.klock.DayOfWeek

data class ClassEntity(
    val id: Int,
    val title: String,
    val typeOfClass: TypeOfClass,
    val classroom: String,
    val classTime: ClassTimeEntity,
    val day: DayOfWeek,
    val lecturer: LecturerEntity,
    val timetableId: Int,
    val needToEmphasize: Boolean
)