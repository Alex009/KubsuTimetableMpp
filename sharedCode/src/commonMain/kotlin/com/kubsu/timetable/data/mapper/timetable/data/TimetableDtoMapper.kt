package com.kubsu.timetable.data.mapper.timetable.data

import com.kubsu.timetable.data.db.timetable.TimetableDb
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto
import com.kubsu.timetable.domain.entity.timetable.data.ClassEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity

object TimetableDtoMapper {
    fun toEntity(
        networkDto: TimetableNetworkDto,
        classList: List<ClassEntity>
    ): TimetableEntity =
        TimetableEntity(
            id = networkDto.id,
            typeOfWeek = TypeOfWeekDtoMapper.toEntity(networkDto.typeOfWeek),
            facultyId = networkDto.facultyId,
            subgroupId = networkDto.subgroupId,
            classList = classList
        )

    fun toEntity(
        timetable: TimetableDb,
        classList: List<ClassEntity>
    ): TimetableEntity =
        TimetableEntity(
            id = timetable.id,
            typeOfWeek = TypeOfWeekDtoMapper.toEntity(timetable.typeOfWeek),
            facultyId = timetable.facultyId,
            subgroupId = timetable.subgroupId,
            classList = classList
        )

    fun toDbDto(entity: TimetableEntity): TimetableDb =
        TimetableDb.Impl(
            id = entity.id,
            typeOfWeek = TypeOfWeekDtoMapper.value(entity.typeOfWeek),
            facultyId = entity.facultyId,
            subgroupId = entity.subgroupId
        )

    fun toDbDto(networkDto: TimetableNetworkDto): TimetableDb =
        TimetableDb.Impl(
            id = networkDto.id,
            typeOfWeek = networkDto.typeOfWeek,
            facultyId = networkDto.facultyId,
            subgroupId = networkDto.subgroupId
        )

    fun toNetworkDto(entity: TimetableEntity): TimetableNetworkDto =
        TimetableNetworkDto(
            id = entity.id,
            typeOfWeek = TypeOfWeekDtoMapper.value(entity.typeOfWeek),
            facultyId = entity.facultyId,
            subgroupId = entity.subgroupId
        )

    fun toNetworkDto(dbDto: TimetableDb): TimetableNetworkDto =
        TimetableNetworkDto(
            id = dbDto.id,
            typeOfWeek = dbDto.typeOfWeek,
            facultyId = dbDto.facultyId,
            subgroupId = dbDto.subgroupId
        )
}