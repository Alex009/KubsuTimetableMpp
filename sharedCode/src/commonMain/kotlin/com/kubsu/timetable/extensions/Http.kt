package com.kubsu.timetable.extensions

import com.kubsu.timetable.data.network.dto.UserNetworkDto
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header

const val sessionId = "sessionid"

fun HttpRequestBuilder.addSessionKey(user: UserNetworkDto) =
    header(sessionId, user.sessionKey)
