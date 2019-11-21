package com.kubsu.timetable.presentation.timetable.model

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

sealed class TypeOfClassModel : Parcelable {
    @Parcelize
    object Lecture : TypeOfClassModel()

    @Parcelize
    object Practice : TypeOfClassModel()
}