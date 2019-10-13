package com.kubsu.timetable.presentation.timetable.model

import com.soywiz.klock.DayOfWeek
import kotlinx.serialization.Serializable

@Serializable
data class TimetableModel(
    val id: Int,
    val typeOfWeek: TypeOfWeekModel,
    val facultyId: Int,
    val subgroupId: Int,
    val infoList: List<TimetableInfoToDisplay>
)

@Serializable
sealed class TimetableInfoToDisplay {
    data class Class(val classModel: ClassModel) : TimetableInfoToDisplay()
    data class Day(val dayOfWeek: DayOfWeek) : TimetableInfoToDisplay()
}