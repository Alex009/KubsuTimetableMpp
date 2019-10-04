package com.kubsu.timetable.data.network.client.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInIncorrectData(
    @SerialName("incorrect_email_or_password")
    val incorrectEmailOrPassword: String? = null,

    @SerialName("account_deleted")
    val accountDeleted: String? = null
)