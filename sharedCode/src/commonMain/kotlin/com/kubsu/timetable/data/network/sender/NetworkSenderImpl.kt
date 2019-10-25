package com.kubsu.timetable.data.network.sender

import com.egroden.teaco.Either
import com.egroden.teaco.left
import com.egroden.teaco.right
import com.kubsu.timetable.data.network.sender.failure.ServerFailure
import com.kubsu.timetable.extensions.readContent
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
import io.ktor.client.response.HttpResponse
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class NetworkSenderImpl(
    private val engine: HttpClientEngine,
    override val json: Json
) : NetworkSender {
    override val apiVersion = "v1"
    override val baseUrl = "https://kubsu-timetable.ru"

    override suspend fun <R> handle(createRequest: suspend HttpClient.() -> R): Either<ServerFailure, R> =
        try {
            Either.right(httpClient.createRequest())
        } catch (e: SerializationException) {
            Either.left(
                ServerFailure.Parsing(e.toString())
            )
        } catch (e: ResponseException) {
            // bad status code
            Either.left(
                ServerFailure.Response(
                    message = e.message,
                    body = e.response.readContent(),
                    code = e.response.status.value
                )
            )
        } catch (e: Exception) {
            Either.left(ServerFailure.Connection(e.message))
        }

    override fun validate(response: HttpResponse) {
        if (response.status.value >= 300) throw ResponseException(response)
    }

    private val httpClient: HttpClient by lazy {
        HttpClient(engine, createConfig())
    }

    private fun createConfig() =
        HttpClientConfig<HttpClientEngineConfig>().apply {
            install(HttpCallValidator) {
                validateResponse { validate(it) }
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }
        }
}