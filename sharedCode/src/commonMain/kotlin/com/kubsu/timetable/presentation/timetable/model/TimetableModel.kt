package com.kubsu.timetable.presentation.timetable.model

import com.kubsu.timetable.platform.SerializableModel
import com.kubsu.timetable.platform.SerializeModel
import com.soywiz.klock.DayOfWeek

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