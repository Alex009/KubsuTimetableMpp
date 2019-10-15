package com.kubsu.timetable.presentation.subscription.model

import platform.SerializableModel
import platform.SerializeModel

@SerializeModel
data class GroupModel(
    val id: Int,
    val number: Int,
    val occupationId: Int
) : SerializableModel