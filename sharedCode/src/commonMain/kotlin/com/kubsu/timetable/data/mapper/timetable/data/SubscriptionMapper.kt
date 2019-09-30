package com.kubsu.timetable.data.mapper.timetable.data

import com.kubsu.timetable.data.db.SubscriptionDb
import com.kubsu.timetable.data.network.dto.timetable.data.SubscriptionNetworkDto
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity

object SubscriptionMapper {
    fun toEntity(
        networkDto: SubscriptionNetworkDto,
        userId: Int
    ): SubscriptionEntity =
        SubscriptionEntity(
            id = networkDto.id,
            title = networkDto.name,
            userId = userId,
            subgroupId = networkDto.subgroup,
            isMain = networkDto.isMain
        )

    fun toEntity(subscription: SubscriptionDb): SubscriptionEntity =
        SubscriptionEntity(
            id = subscription.id,
            title = subscription.name,
            userId = subscription.userId,
            subgroupId = subscription.subgroupId,
            isMain = subscription.isMain
        )

    fun toDbDto(entity: SubscriptionEntity): SubscriptionDb =
        SubscriptionDb.Impl(
            id = entity.id,
            name = entity.title,
            userId = entity.userId,
            subgroupId = entity.subgroupId,
            isMain = entity.isMain
        )

    fun toDbDto(subscription: SubscriptionNetworkDto, userId: Int): SubscriptionDb =
        SubscriptionDb.Impl(
            id = subscription.id,
            name = subscription.name,
            userId = userId,
            subgroupId = subscription.subgroup,
            isMain = subscription.isMain
        )

    fun toNetworkDto(entity: SubscriptionEntity): SubscriptionNetworkDto =
        SubscriptionNetworkDto(
            id = entity.id,
            name = entity.title,
            subgroup = entity.subgroupId,
            isMain = entity.isMain
        )

    fun toNetworkDto(dbDto: SubscriptionDb): SubscriptionNetworkDto =
        SubscriptionNetworkDto(
            id = dbDto.id,
            name = dbDto.name,
            subgroup = dbDto.subgroupId,
            isMain = dbDto.isMain
        )
}