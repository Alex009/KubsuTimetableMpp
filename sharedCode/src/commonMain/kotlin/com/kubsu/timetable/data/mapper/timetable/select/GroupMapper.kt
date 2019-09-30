package com.kubsu.timetable.data.mapper.timetable.select

import com.kubsu.timetable.data.network.dto.timetable.select.GroupNetworkDto
import com.kubsu.timetable.domain.entity.timetable.select.GroupEntity

object GroupMapper {
    fun toEntity(
        networkDto: GroupNetworkDto,
        occupationId: Int
    ): GroupEntity =
        GroupEntity(
            id = networkDto.id,
            number = networkDto.number,
            occupationId = occupationId
        )

    fun toNetworkDto(entity: GroupEntity): GroupNetworkDto =
        GroupNetworkDto(
            id = entity.id,
            number = entity.number
        )
}