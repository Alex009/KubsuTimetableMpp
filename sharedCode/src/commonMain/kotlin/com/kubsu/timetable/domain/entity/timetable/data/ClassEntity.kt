package com.kubsu.timetable.domain.entity.timetable.data

import com.kubsu.timetable.domain.entity.Day

data class ClassEntity(
    val id: Int,
    val title: String,
    val typeOfClass: TypeOfClass,
    val classroom: String,
    val classTime: ClassTimeEntity,
    val day: Day,
    val lecturer: LecturerEntity,
    val timetableId: Int
)