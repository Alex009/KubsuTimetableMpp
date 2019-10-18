package com.kubsu.timetable.data.network.dto

import com.kubsu.timetable.extensions.sessionId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserNetworkDto(
    val id: Int,

    val email: String,

    @SerialName("first_name")
    val firstName: String,

    @SerialName("last_name")
    val lastName: String,

    @SerialName(sessionId)
    val sessionKey: String
)