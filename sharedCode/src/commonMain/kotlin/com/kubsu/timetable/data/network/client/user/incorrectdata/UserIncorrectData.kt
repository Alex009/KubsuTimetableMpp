package com.kubsu.timetable.data.network.client.user.incorrectdata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserIncorrectData(
    @SerialName("first_name")
    val firstName: List<String> = listOf(),

    @SerialName("last_name")
    val lastName: List<String> = listOf(),

    val email: List<String> = listOf(),

    val password: List<String> = listOf()
)