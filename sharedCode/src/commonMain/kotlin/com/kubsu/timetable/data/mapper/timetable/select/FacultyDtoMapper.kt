package com.kubsu.timetable.data.mapper.timetable.select

import com.kubsu.timetable.data.network.dto.timetable.select.FacultyNetworkDto
import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity

object FacultyDtoMapper {
    fun toEntity(networkDto: FacultyNetworkDto): FacultyEntity =
        FacultyEntity(
            id = networkDto.id,
            title = networkDto.title
        )

    fun toNetworkDto(entity: FacultyEntity): FacultyNetworkDto =
        FacultyNetworkDto(
            id = entity.id,
            title = entity.title
        )
}