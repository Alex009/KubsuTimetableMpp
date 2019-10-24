package com.kubsu.timetable.data.network.sender

import com.egroden.teaco.Either
import com.kubsu.timetable.data.network.sender.failure.ServerFailure
import io.ktor.client.HttpClient
import io.ktor.client.features.ResponseException
import io.ktor.client.response.HttpResponse
import kotlinx.serialization.json.Json

interface NetworkSender {
    val apiVersion: String
    val baseUrl: String
    val json: Json

    suspend fun <R> handle(createRequest: suspend HttpClient.() -> R): Either<ServerFailure, R>

    /**
     * @throws ResponseException if response code is not 2**.
     */
    fun validate(response: HttpResponse)
}