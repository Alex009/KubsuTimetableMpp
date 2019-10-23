package com.kubsu.timetable.extensions

import com.kubsu.timetable.data.storage.user.session.SessionDto
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header

const val sessionId = "sessionid"

fun HttpRequestBuilder.addSessionKey(session: SessionDto) =
    header(sessionId, session.id)
