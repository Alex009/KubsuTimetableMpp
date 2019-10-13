package com.kubsu.timetable.presentation.timetable.mapper

import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel

object SubscriptionModelMapper {
    fun toEntity(model: SubscriptionModel): SubscriptionEntity =
        SubscriptionEntity(
            id = model.id,
            title = model.title,
            userId = model.userId,
            subgroupId = model.subgroupId,
            isMain = model.isMain
        )

    fun toModel(entity: SubscriptionEntity): SubscriptionModel =
        SubscriptionModel(
            id = entity.id,
            title = entity.title,
            subgroupId = entity.subgroupId,
            isMain = entity.isMain,
            userId = entity.userId
        )
}