package com.kubsu.timetable.data.mapper.timetable.select

import com.kubsu.timetable.data.network.dto.timetable.select.SubgroupNetworkDto
import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity

object SubgroupDtoMapper {
    fun toEntity(
        networkDto: SubgroupNetworkDto,
        groupId: Int
    ): SubgroupEntity =
        SubgroupEntity(
            id = networkDto.id,
            number = networkDto.number,
            groupId = groupId
        )

    fun toNetworkDto(entity: SubgroupEntity): SubgroupNetworkDto =
        SubgroupNetworkDto(
            id = entity.id,
            number = entity.number
        )
}