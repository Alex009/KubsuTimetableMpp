package com.kubsu.timetable.presentation.timetable.model

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

sealed class TypeOfWeekModel : Parcelable {
    @Parcelize
    object Numerator : TypeOfWeekModel()

    @Parcelize
    object Denominator : TypeOfWeekModel()
}