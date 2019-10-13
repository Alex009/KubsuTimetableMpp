package com.kubsu.timetable.presentation.subscription.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupModel(
    val id: Int,
    val number: Int,
    val occupationId: Int
)