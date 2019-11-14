package com.kubsu.timetable.presentation.subscription.model

import com.kubsu.timetable.platform.SerializableModel
import com.kubsu.timetable.platform.SerializeModel

@SerializeModel
data class GroupModel(
    val id: Int,
    val number: Int,
    val occupationId: Int
) : SerializableModel