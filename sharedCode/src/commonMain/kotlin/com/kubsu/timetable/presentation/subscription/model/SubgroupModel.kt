package com.kubsu.timetable.presentation.subscription.model

import platform.SerializableModel
import platform.SerializeModel

@SerializeModel
data class SubgroupModel(
    val id: Int,
    val number: Int,
    val groupId: Int
) : SerializableModel