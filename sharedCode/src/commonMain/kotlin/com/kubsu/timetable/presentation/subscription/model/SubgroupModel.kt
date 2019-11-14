package com.kubsu.timetable.presentation.subscription.model

import com.kubsu.timetable.platform.SerializableModel
import com.kubsu.timetable.platform.SerializeModel

@SerializeModel
data class SubgroupModel(
    val id: Int,
    val number: Int,
    val groupId: Int
) : SerializableModel