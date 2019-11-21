package com.kubsu.timetable.presentation.subscription.model

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

@Parcelize
data class SubgroupModel(
    val id: Int,
    val number: Int,
    val groupId: Int
) : Parcelable