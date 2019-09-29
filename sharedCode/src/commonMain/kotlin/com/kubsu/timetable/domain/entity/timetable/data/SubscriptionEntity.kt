package com.kubsu.timetable.domain.entity.timetable.data

data class SubscriptionEntity(
    val id: Int,
    val title: String,
    val userId: Int,
    val subgroupId: Int,
    val isMain: Boolean
)