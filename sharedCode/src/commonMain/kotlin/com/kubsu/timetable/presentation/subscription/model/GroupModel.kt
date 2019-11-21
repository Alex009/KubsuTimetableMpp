package com.kubsu.timetable.presentation.subscription.model

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

@Parcelize
data class GroupModel(
    val id: Int,
    val number: Int,
    val occupationId: Int
) : Parcelable