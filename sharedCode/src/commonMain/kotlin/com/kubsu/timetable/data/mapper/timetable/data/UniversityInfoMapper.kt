package com.kubsu.timetable.data.mapper.timetable.data

import com.kubsu.timetable.data.db.timetable.UniversityInfoDb
import com.kubsu.timetable.data.network.dto.timetable.data.UniversityInfoNetworkDto
import com.kubsu.timetable.domain.entity.timetable.data.UniversityInfoEntity

object UniversityInfoMapper {
    fun toEntity(networkDto: UniversityInfoNetworkDto): UniversityInfoEntity =
        UniversityInfoEntity(
            facultyId = networkDto.facultyId,
            typeOfWeek = TypeOfWeekMapper.toEntity(networkDto.typeOfWeek)
        )

    fun toEntity(dbDto: UniversityInfoDb): UniversityInfoEntity =
        UniversityInfoEntity(
            facultyId = dbDto.facultyId,
            typeOfWeek = TypeOfWeekMapper.toEntity(dbDto.typeOfWeek)
        )

    fun toNetworkDto(entity: UniversityInfoEntity): UniversityInfoNetworkDto =
        UniversityInfoNetworkDto(
            facultyId = entity.facultyId,
            typeOfWeek = TypeOfWeekMapper.value(entity.typeOfWeek)
        )

    fun toNetworkDto(dbDto: UniversityInfoDb): UniversityInfoNetworkDto =
        UniversityInfoNetworkDto(
            facultyId = dbDto.facultyId,
            typeOfWeek = dbDto.typeOfWeek
        )

    fun toDbDto(networkDto: UniversityInfoNetworkDto): UniversityInfoDb =
        UniversityInfoDb.Impl(
            facultyId = networkDto.facultyId,
            typeOfWeek = networkDto.typeOfWeek
        )

    fun toDbDto(entity: UniversityInfoEntity): UniversityInfoDb =
        UniversityInfoDb.Impl(
            facultyId = entity.facultyId,
            typeOfWeek = TypeOfWeekMapper.value(entity.typeOfWeek)
        )
}