package com.kubsu.timetable.data.mapper.timetable.data

import com.kubsu.timetable.data.db.timetable.ClassDb
import com.kubsu.timetable.data.mapper.DayMapper
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.domain.entity.timetable.data.ClassEntity
import com.kubsu.timetable.domain.entity.timetable.data.ClassTimeEntity
import com.kubsu.timetable.domain.entity.timetable.data.LecturerEntity

object ClassMapper {
    fun toEntity(
        networkDto: ClassNetworkDto,
        timetableId: Int,
        classTime: ClassTimeEntity,
        lecturer: LecturerEntity
    ): ClassEntity =
        ClassEntity(
            id = networkDto.id,
            title = networkDto.title,
            typeOfClass = TypeOfClassMapper.toEntity(networkDto.typeOfClass),
            classroom = networkDto.classroom,
            classTime = classTime,
            day = DayMapper.toEntity(networkDto.weekday),
            lecturer = lecturer,
            timetableId = timetableId
        )

    fun toEntity(
        dbDto: ClassDb,
        classTime: ClassTimeEntity,
        lecturer: LecturerEntity
    ): ClassEntity =
        ClassEntity(
            id = dbDto.id,
            title = dbDto.title,
            typeOfClass = TypeOfClassMapper.toEntity(dbDto.typeOfClass),
            classroom = dbDto.classroom,
            classTime = classTime,
            day = DayMapper.toEntity(dbDto.day),
            lecturer = lecturer,
            timetableId = dbDto.timetableId
        )

    fun toDbDto(
        entity: ClassEntity
    ): ClassDb =
        ClassDb.Impl(
            id = entity.id,
            title = entity.title,
            typeOfClass = TypeOfClassMapper.value(entity.typeOfClass),
            classroom = entity.classroom,
            classTimeId = entity.classTime.id,
            day = DayMapper.value(entity.day),
            lecturerId = entity.lecturer.id,
            timetableId = entity.timetableId
        )

    fun toDbDto(
        networkDto: ClassNetworkDto,
        timetableId: Int
    ): ClassDb =
        ClassDb.Impl(
            id = networkDto.id,
            title = networkDto.title,
            typeOfClass = networkDto.typeOfClass,
            classroom = networkDto.classroom,
            classTimeId = networkDto.classTimeId,
            day = networkDto.weekday,
            lecturerId = networkDto.lecturerId,
            timetableId = timetableId
        )

    fun toNetworkDto(
        entity: ClassEntity
    ): ClassNetworkDto =
        ClassNetworkDto(
            id = entity.id,
            title = entity.title,
            typeOfClass = TypeOfClassMapper.value(entity.typeOfClass),
            classroom = entity.classroom,
            classTimeId = entity.classTime.id,
            weekday = DayMapper.value(entity.day),
            lecturerId = entity.lecturer.id
        )

    fun toNetworkDto(
        dbDto: ClassDb
    ): ClassNetworkDto =
        ClassNetworkDto(
            id = dbDto.id,
            title = dbDto.title,
            typeOfClass = dbDto.typeOfClass,
            classroom = dbDto.classroom,
            classTimeId = dbDto.classTimeId,
            weekday = dbDto.day,
            lecturerId = dbDto.lecturerId
        )
}