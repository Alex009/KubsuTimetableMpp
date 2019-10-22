package com.kubsu.timetable.presentation.timetable.model

import com.soywiz.klock.DayOfWeek
import platform.SerializableModel
import platform.SerializeModel

@SerializeModel
data class TimetableModel(
    val id: Int,
    val typeOfWeek: TypeOfWeekModel,
    val facultyId: Int,
    val subgroupId: Int,
    val infoList: List<TimetableInfoToDisplay>
) : SerializableModel

sealed class TimetableInfoToDisplay : SerializableModel {
    @SerializeModel
    data class Class(val classModel: ClassModel) : TimetableInfoToDisplay()

    @SerializeModel
    data class Day(
        val index: Int,
        val dayOfWeek: DayOfWeek
    ) : TimetableInfoToDisplay()
}