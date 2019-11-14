package com.kubsu.timetable.presentation.timetable.model

import com.kubsu.timetable.platform.SerializableModel
import com.kubsu.timetable.platform.SerializeModel

@SerializeModel
data class SubscriptionModel(
    val id: Int,
    val title: String,
    val userId: Int,
    val subgroupId: Int,
    val isMain: Boolean,
    val numberOfUpdatedClasses: Long
) : SerializableModel