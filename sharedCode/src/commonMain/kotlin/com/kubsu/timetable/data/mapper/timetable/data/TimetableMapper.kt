package com.kubsu.timetable.data.mapper.timetable.data

import com.kubsu.timetable.data.db.TimetableDb
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto
import com.kubsu.timetable.domain.entity.timetable.data.ClassEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.domain.entity.timetable.data.TypeOfWeek

object TimetableMapper {
    fun toEntity(
        networkDto: TimetableNetworkDto,
        classList: List<ClassEntity>
    ): TimetableEntity =
        TimetableEntity(
            id = networkDto.id,
            typeOfWeek = TypeOfWeek.convert(networkDto.typeOfWeek),
            subgroupId = networkDto.subgroupId,
            classList = classList
        )

    fun toEntity(
        timetable: TimetableDb,
        classList: List<ClassEntity>
    ): TimetableEntity =
        TimetableEntity(
            id = timetable.id,
            typeOfWeek = TypeOfWeek.convert(timetable.typeOfWeek),
            subgroupId = timetable.subgroupId,
            classList = classList
        )

    fun toDbDto(entity: TimetableEntity): TimetableDb =
        TimetableDb.Impl(
            id = entity.id,
            typeOfWeek = entity.typeOfWeek.number,
            subgroupId = entity.subgroupId
        )

    fun toDbDto(networkDto: TimetableNetworkDto): TimetableDb =
        TimetableDb.Impl(
            id = networkDto.id,
            typeOfWeek = networkDto.typeOfWeek,
            subgroupId = networkDto.subgroupId
        )

    fun toNetworkDto(entity: TimetableEntity): TimetableNetworkDto =
        TimetableNetworkDto(
            id = entity.id,
            typeOfWeek = entity.typeOfWeek.number,
            subgroupId = entity.subgroupId
        )

    fun toNetworkDto(dbDto: TimetableDb): TimetableNetworkDto =
        TimetableNetworkDto(
            id = dbDto.id,
            typeOfWeek = dbDto.typeOfWeek,
            subgroupId = dbDto.subgroupId
        )
}