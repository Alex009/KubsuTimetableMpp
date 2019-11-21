package com.kubsu.timetable.presentation.timetable.model

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

@Parcelize
data class ClassTimeModel(
    val id: Int,
    val number: Int,
    val start: String,
    val end: String
) : Parcelable