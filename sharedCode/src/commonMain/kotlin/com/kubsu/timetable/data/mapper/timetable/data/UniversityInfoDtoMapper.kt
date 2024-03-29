package com.kubsu.timetable.data.mapper.timetable.data

import com.kubsu.timetable.data.db.timetable.UniversityInfoDb
import com.kubsu.timetable.data.network.dto.timetable.data.UniversityInfoNetworkDto
import com.kubsu.timetable.domain.entity.timetable.data.UniversityInfoEntity
import com.kubsu.timetable.extensions.getWeekNumber

object UniversityInfoDtoMapper {
    fun toEntity(networkDto: UniversityInfoNetworkDto): UniversityInfoEntity =
        UniversityInfoEntity(
            id = networkDto.id,
            facultyId = networkDto.facultyId,
            typeOfWeek = TypeOfWeekDtoMapper.toEntity(networkDto.typeOfWeek),
            weekNumber = getWeekNumber()
        )

    fun toEntity(dbDto: UniversityInfoDb): UniversityInfoEntity =
        UniversityInfoEntity(
            id = dbDto.id,
            facultyId = dbDto.facultyId,
            typeOfWeek = TypeOfWeekDtoMapper.toEntity(dbDto.typeOfWeek),
            weekNumber = dbDto.weekNumber
        )

    fun toNetworkDto(entity: UniversityInfoEntity): UniversityInfoNetworkDto =
        UniversityInfoNetworkDto(
            id = entity.id,
            facultyId = entity.facultyId,
            typeOfWeek = TypeOfWeekDtoMapper.value(entity.typeOfWeek)
        )

    fun toNetworkDto(dbDto: UniversityInfoDb): UniversityInfoNetworkDto =
        UniversityInfoNetworkDto(
            id = dbDto.id,
            facultyId = dbDto.facultyId,
            typeOfWeek = dbDto.typeOfWeek
        )

    fun toDbDto(networkDto: UniversityInfoNetworkDto): UniversityInfoDb =
        UniversityInfoDb.Impl(
            id = networkDto.id,
            facultyId = networkDto.facultyId,
            typeOfWeek = networkDto.typeOfWeek,
            weekNumber = getWeekNumber()
        )

    fun toDbDto(entity: UniversityInfoEntity): UniversityInfoDb =
        UniversityInfoDb.Impl(
            id = entity.id,
            facultyId = entity.facultyId,
            typeOfWeek = TypeOfWeekDtoMapper.value(entity.typeOfWeek),
            weekNumber = entity.weekNumber
        )
}