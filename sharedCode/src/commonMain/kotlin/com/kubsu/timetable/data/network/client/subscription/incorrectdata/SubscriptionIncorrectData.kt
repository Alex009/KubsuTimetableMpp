package com.kubsu.timetable.data.network.client.subscription.incorrectdata

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionIncorrectData(
    val title: List<String> = listOf(),
    val subgroup: List<String> = listOf()
)