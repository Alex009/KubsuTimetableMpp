package com.kubsu.timetable

import com.kubsu.timetable.data.network.dto.UserNetworkDto
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.append
import io.ktor.http.content.TextContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.content

suspend inline fun <T> def(noinline block: suspend CoroutineScope.() -> T): T =
    withContext(Dispatchers.Default, block = block)

fun HttpRequestBuilder.addSessionKey(user: UserNetworkDto) =
    header("sessionid", user.sessionKey)

fun jsonContent(map: Map<String, JsonElement>): TextContent =
    TextContent(
        text = JsonObject(map).toString(),
        contentType = ContentType.Application.Json
    )

fun <T> flowOfIterable(iterable: Iterable<T>): Flow<T> = flow {
    for (element in iterable)
        emit(element)
}

class StopException : Exception()

suspend inline fun <L, R> Flow<Either<L, R>>.collectRightListOrFirstLeft(): Either<L, List<R>> {
    val result = ArrayList<R>()
    var fail: L? = null
    try {
        collect {
            it.fold(
                ifRight = result::add,
                ifLeft = { left ->
                    fail = left
                    throw StopException()
                }
            )
        }
    } catch (e: StopException) {
        // Do nothing
    }
    return fail?.let { Either.left(it) } ?: Either.right(result)
}