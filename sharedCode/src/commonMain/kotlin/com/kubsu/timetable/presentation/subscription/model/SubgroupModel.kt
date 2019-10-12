package com.kubsu.timetable.presentation.subscription.model

import kotlinx.serialization.Serializable

@Serializable
data class SubgroupModel(
    val id: Int,
    val number: Int,
    val groupId: Int
)