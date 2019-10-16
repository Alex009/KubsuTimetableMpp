package com.kubsu.timetable.data.network.client.user.incorrectdata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInIncorrectData(
    @SerialName("__all__")
    val all: List<String> = listOf(),

    val email: List<String> = listOf(),

    val password: List<String> = listOf()
)