package com.kubsu.timetable.extensions

import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import kotlinx.serialization.json.*
import kotlin.jvm.JvmName

fun jsonContent(vararg params: Pair<String, JsonElement>): TextContent =
    TextContent(
        text = JsonObject(params.toMap()).toString(),
        contentType = ContentType.Application.Json
    )

fun Number.toJson(): JsonPrimitive =
    JsonLiteral(this)

fun String.toJson(): JsonPrimitive =
    JsonLiteral(this)

fun Boolean.toJson(): JsonPrimitive =
    JsonLiteral(this)

@JvmName("toJsonNumberList")
fun List<Number>.toJson(): JsonArray =
    JsonArray(map(::JsonLiteral))

@JvmName("toJsonStringList")
fun List<String>.toJson(): JsonArray =
    JsonArray(map(::JsonLiteral))

@JvmName("toJsonBooleanList")
fun List<Boolean>.toJson(): JsonArray =
    JsonArray(map(::JsonLiteral))

fun Map<String, String>.toJson(): JsonObject =
    JsonObject(
        map { (key, value) -> key to value.toJson() }.toMap()
    )