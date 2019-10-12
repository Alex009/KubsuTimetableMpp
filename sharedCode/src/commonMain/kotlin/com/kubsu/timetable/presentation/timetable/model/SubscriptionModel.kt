package com.kubsu.timetable.presentation.timetable.model

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionModel(
    val id: Int,
    val title: String,
    val userId: Int,
    val subgroupId: Int,
    val isMain: Boolean
)