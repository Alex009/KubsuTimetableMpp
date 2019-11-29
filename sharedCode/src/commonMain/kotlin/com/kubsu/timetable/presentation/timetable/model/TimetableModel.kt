package com.kubsu.timetable.presentation.timetable.model

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize
import com.kubsu.timetable.presentation.timetable.mapper.DayModelMapper
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
        val dayOfWeek: DayOfWeek
    ) : TimetableInfoToDisplay() {
        val index: Int
            get() = DayModelMapper.value(dayOfWeek)
    }
}