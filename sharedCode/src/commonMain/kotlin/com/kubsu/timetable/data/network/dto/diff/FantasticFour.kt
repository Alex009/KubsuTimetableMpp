package com.kubsu.timetable.data.network.dto.diff

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

@Serializable
class FantasticFour(
    val id: Int,

    @SerialName("object_id")
    val objectId: Int,

    val data: Map<String, JsonPrimitive>
)