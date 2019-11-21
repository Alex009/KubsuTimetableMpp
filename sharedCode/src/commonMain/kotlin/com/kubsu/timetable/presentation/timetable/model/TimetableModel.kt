package com.kubsu.timetable.presentation.timetable.model

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize
import com.soywiz.klock.DayOfWeek

@Parcelize
data class TimetableModel(
    val id: Int,
    val typeOfWeek: TypeOfWeekModel,
    val facultyId: Int,
    val subgroupId: Int,
    val infoList: List<TimetableInfoToDisplay>
) : Parcelable

sealed class TimetableInfoToDisplay : Parcelable {
    @Parcelize
    data class Class(val classModel: ClassModel) : TimetableInfoToDisplay()

    @Parcelize
    data class Day(
        val index: Int,
        val dayOfWeek: DayOfWeek
    ) : TimetableInfoToDisplay()
}