package com.kubsu.timetable.presentation.timetable.model

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

@Parcelize
data class LecturerModel(
    val id: Int,
    val name: String,
    val surname: String,
    val patronymic: String
) : Parcelable