package com.kubsu.timetable.data.mapper.timetable.data

import com.kubsu.timetable.data.db.timetable.ClassDb
import com.kubsu.timetable.data.mapper.DayDtoMapper
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.domain.entity.timetable.data.ClassEntity
import com.kubsu.timetable.domain.entity.timetable.data.ClassTimeEntity
import com.kubsu.timetable.domain.entity.timetable.data.LecturerEntity

object ClassDtoMapper {
    fun toEntity(
        networkDto: ClassNetworkDto,
        classTime: ClassTimeEntity,
        lecturer: LecturerEntity,
        needToEmphasize: Boolean
    ): ClassEntity =
        ClassEntity(
            id = networkDto.id,
            title = networkDto.title,
            typeOfClass = TypeOfClassDtoMapper.toEntity(networkDto.typeOfClass),
            classroom = networkDto.classroom,
            classTime = classTime,
            day = DayDtoMapper.toEntity(networkDto.weekday),
            lecturer = lecturer,
            timetableId = networkDto.timetableId,
            needToEmphasize = needToEmphasize
        )

    fun toEntity(
        dbDto: ClassDb,
        classTime: ClassTimeEntity,
        lecturer: LecturerEntity
    ): ClassEntity =
        ClassEntity(
            id = dbDto.id,
            title = dbDto.title,
            typeOfClass = TypeOfClassDtoMapper.toEntity(dbDto.typeOfClass),
            classroom = dbDto.classroom,
            classTime = classTime,
            day = DayDtoMapper.toEntity(dbDto.day),
            lecturer = lecturer,
            timetableId = dbDto.timetableId,
            needToEmphasize = dbDto.needToEmphasize
        )

    fun toDbDto(
        entity: ClassEntity
    ): ClassDb =
        ClassDb.Impl(
            id = entity.id,
            title = entity.title,
            typeOfClass = TypeOfClassDtoMapper.value(entity.typeOfClass),
            classroom = entity.classroom,
            classTimeId = entity.classTime.id,
            day = DayDtoMapper.value(entity.day),
            lecturerId = entity.lecturer.id,
            timetableId = entity.timetableId,
            needToEmphasize = entity.needToEmphasize
        )

    fun toDbDto(
        networkDto: ClassNetworkDto,
        needToEmphasize: Boolean
    ): ClassDb =
        ClassDb.Impl(
            id = networkDto.id,
            title = networkDto.title,
            typeOfClass = networkDto.typeOfClass,
            classroom = networkDto.classroom,
            classTimeId = networkDto.classTimeId,
            day = networkDto.weekday,
            lecturerId = networkDto.lecturerId,
            timetableId = networkDto.timetableId,
            needToEmphasize = needToEmphasize
        )

    fun toNetworkDto(
        entity: ClassEntity
    ): ClassNetworkDto =
        ClassNetworkDto(
            id = entity.id,
            title = entity.title,
            typeOfClass = TypeOfClassDtoMapper.value(entity.typeOfClass),
            classroom = entity.classroom,
            classTimeId = entity.classTime.id,
            weekday = DayDtoMapper.value(entity.day),
            lecturerId = entity.lecturer.id,
            timetableId = entity.timetableId
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
            lecturerId = dbDto.lecturerId,
            timetableId = dbDto.timetableId
        )
}