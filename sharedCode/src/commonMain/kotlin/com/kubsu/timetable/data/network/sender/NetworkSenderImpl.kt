package com.kubsu.timetable.data.network.sender

import com.kubsu.timetable.Either
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

class NetworkSenderImpl(private val engine: HttpClientEngine) : NetworkSender {
    override val apiVersion = "v1"
    override val baseUrl = "http://kubsu-timetable.info"

    override suspend fun <L, R> handle(block: suspend HttpClient.() -> R): Either<L, R> {
        /*try {
            Either.right(httpClient.block())
        } catch (e: RedirectResponseException) { // statusCode 300..399

        } catch (e: ClientRequestException) { // statusCode 400..499

        } catch (e: ServerResponseException) { // statusCode 500..599

        } catch (e: ResponseException) { // statusCode >= 600

        }*/
        return Either.right(httpClient.block())
    }

    private val httpClient: HttpClient by lazy {
        HttpClient(engine, createConfig())
    }

    @UseExperimental(UnstableDefault::class)
    private fun createConfig() =
        HttpClientConfig<HttpClientEngineConfig>().apply {
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json.nonstrict)
            }
        }
}