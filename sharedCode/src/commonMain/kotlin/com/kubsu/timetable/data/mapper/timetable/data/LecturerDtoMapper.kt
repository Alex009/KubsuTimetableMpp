package com.kubsu.timetable.data.mapper.timetable.data

import com.kubsu.timetable.data.db.timetable.LecturerDb
import com.kubsu.timetable.data.network.dto.timetable.data.LecturerNetworkDto
import com.kubsu.timetable.domain.entity.timetable.data.LecturerEntity

object LecturerDtoMapper {
    fun toEntity(networkDto: LecturerNetworkDto): LecturerEntity =
        LecturerEntity(
            id = networkDto.id,
            name = networkDto.name,
            surname = networkDto.surname,
            patronymic = networkDto.patronymic
        )

    fun toEntity(lecturer: LecturerDb): LecturerEntity =
        LecturerEntity(
            id = lecturer.id,
            name = lecturer.name,
            surname = lecturer.surname,
            patronymic = lecturer.patronymic
        )

    fun toDbDto(entity: LecturerEntity): LecturerDb =
        LecturerDb.Impl(
            id = entity.id,
            name = entity.name,
            surname = entity.surname,
            patronymic = entity.patronymic
        )

    fun toDbDto(networkDto: LecturerNetworkDto): LecturerDb =
        LecturerDb.Impl(
            id = networkDto.id,
            name = networkDto.name,
            surname = networkDto.surname,
            patronymic = networkDto.patronymic
        )

    fun toNetworkDto(entity: LecturerEntity): LecturerNetworkDto =
        LecturerNetworkDto(
            id = entity.id,
            name = entity.name,
            surname = entity.surname,
            patronymic = entity.patronymic
        )

    fun toNetworkDto(dbDto: LecturerDb): LecturerNetworkDto =
        LecturerNetworkDto(
            id = dbDto.id,
            name = dbDto.name,
            surname = dbDto.surname,
            patronymic = dbDto.patronymic
        )
}