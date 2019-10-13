package com.kubsu.timetable.data.mapper.timetable.data

import com.kubsu.timetable.data.db.timetable.ClassTimeDb
import com.kubsu.timetable.data.network.dto.timetable.data.ClassTimeNetworkDto
import com.kubsu.timetable.domain.entity.timetable.data.ClassTimeEntity

object ClassTimeDtoMapper {
    fun toEntity(networkDto: ClassTimeNetworkDto): ClassTimeEntity =
        ClassTimeEntity(
            id = networkDto.id,
            number = networkDto.number,
            start = networkDto.start,
            end = networkDto.end
        )

    fun toEntity(classTime: ClassTimeDb): ClassTimeEntity =
        ClassTimeEntity(
            id = classTime.id,
            number = classTime.number,
            start = classTime.start,
            end = classTime.end
        )

    fun toDbDto(entity: ClassTimeEntity): ClassTimeDb =
        ClassTimeDb.Impl(
            id = entity.id,
            number = entity.number,
            start = entity.start,
            end = entity.end
        )

    fun toDbDto(networkDto: ClassTimeNetworkDto): ClassTimeDb =
        ClassTimeDb.Impl(
            id = networkDto.id,
            number = networkDto.number,
            start = networkDto.start,
            end = networkDto.end
        )

    fun toNetworkDto(entity: ClassTimeEntity): ClassTimeNetworkDto =
        ClassTimeNetworkDto(
            id = entity.id,
            number = entity.number,
            start = entity.start,
            end = entity.end
        )

    fun toNetworkDto(dbDto: ClassTimeDb): ClassTimeNetworkDto =
        ClassTimeNetworkDto(
            id = dbDto.id,
            number = dbDto.number,
            start = dbDto.start,
            end = dbDto.end
        )
}