package com.kubsu.timetable.data.network.client.user

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationIncorrectData(
    val email: List<String> = listOf(),
    val password: List<String> = listOf()
)