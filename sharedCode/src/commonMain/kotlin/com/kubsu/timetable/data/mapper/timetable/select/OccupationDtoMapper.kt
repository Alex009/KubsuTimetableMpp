package com.kubsu.timetable.data.mapper.timetable.select

import com.kubsu.timetable.data.network.dto.timetable.select.OccupationNetworkDto
import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity

object OccupationDtoMapper {
    fun toEntity(
        networkDto: OccupationNetworkDto,
        facultyId: Int
    ): OccupationEntity =
        OccupationEntity(
            id = networkDto.id,
            title = networkDto.title,
            code = networkDto.code,
            facultyId = facultyId
        )

    fun toNetworkDto(entity: OccupationEntity): OccupationNetworkDto =
        OccupationNetworkDto(
            id = entity.id,
            title = entity.title,
            code = entity.code
        )
}