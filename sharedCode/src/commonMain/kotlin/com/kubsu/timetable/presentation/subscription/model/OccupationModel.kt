package com.kubsu.timetable.presentation.subscription.model

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

@Parcelize
data class OccupationModel(
    val id: Int,
    val title: String,
    val code: String,
    val facultyId: Int
) : Parcelable