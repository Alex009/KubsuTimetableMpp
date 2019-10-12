package com.kubsu.timetable.presentation.subscription.mapper

import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity
import com.kubsu.timetable.presentation.subscription.model.GroupModel

object GroupModelMapper {
    fun toModel(entity: GroupEntity): GroupModel =
        GroupModel(
            id = entity.id,
            number = entity.number,
            occupationId = entity.occupationId
        )

    fun toEntity(model: GroupModel): GroupEntity =
        GroupEntity(
            id = model.id,
            number = model.number,
            occupationId = model.occupationId
        )
}