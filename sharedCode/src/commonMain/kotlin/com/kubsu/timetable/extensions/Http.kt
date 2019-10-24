package com.kubsu.timetable.extensions

import com.kubsu.timetable.data.storage.user.session.SessionDto
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.response.HttpResponse
import io.ktor.utils.io.readRemaining

const val sessionId = "sessionid"

fun HttpRequestBuilder.addSessionKey(session: SessionDto) =
    header(sessionId, session.id)

suspend fun HttpResponse.readContent() =
    content.readRemaining().readText()