package platform

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.ios.IosClientEngineConfig
import io.ktor.client.features.json.JsonFeature

val v = HttpClientConfig<IosClientEngineConfig>().apply {
    install(JsonFeature) {
        serializer
    }
}