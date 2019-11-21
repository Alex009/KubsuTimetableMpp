package com.kubsu.timetable.presentation.subscription.model

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

@Parcelize
data class FacultyModel(
    val id: Int,
    val title: String
) : Parcelable