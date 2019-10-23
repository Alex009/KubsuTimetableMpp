package com.kubsu.timetable.data.network.dto.diff

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SyncResponse(
    @SerialName("updated_ids")
    val updatedIds: List<Int>,

    @SerialName("deleted_ids")
    val deletedIds: List<Int>
)