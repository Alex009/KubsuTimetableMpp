package com.kubsu.timetable.data.network.client.subscription.incorrectdata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionIncorrectData(
    val title: List<String> = listOf(),
    val subgroup: List<String> = listOf(),

    @SerialName("non_field_errors")
    val nonFieldErrors: List<String> = listOf()
)