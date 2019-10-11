package com.kubsu.timetable.data.network.sender

import com.kubsu.timetable.Either
import com.kubsu.timetable.data.network.sender.failure.ServerFailure
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.features.HttpCallValidator
import io.ktor.client.features.ResponseException
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import io.ktor.utils.io.readRemaining
import kotlinx.serialization.json.Json

class NetworkSenderImpl(
    private val engine: HttpClientEngine,
    private val json: Json
) : NetworkSender {
    override val apiVersion = "v1"
    override val baseUrl = "https://kubsu-timetable.info"

    override suspend fun <R> handle(createRequest: suspend HttpClient.() -> R): Either<ServerFailure, R> =
        try {
            Either.right(httpClient.createRequest())
        } catch (e: ResponseException) {
            // bad status code
            Either.left(
                ServerFailure.Response(
                    message = e.message,
                    body = e.response.content.readRemaining().readText(),
                    code = e.response.status.value
                )
            )
        } catch (e: Exception) {
            Either.left(ServerFailure.Connection(e.message))
        }

    private val httpClient: HttpClient by lazy {
        HttpClient(engine, createConfig())
    }

    private fun createConfig() =
        HttpClientConfig<HttpClientEngineConfig>().apply {
            install(HttpCallValidator) {
                validateResponse { response ->
                    if (response.status.value >= 300) throw ResponseException(response)
                }
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.NONE
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }
        }
}