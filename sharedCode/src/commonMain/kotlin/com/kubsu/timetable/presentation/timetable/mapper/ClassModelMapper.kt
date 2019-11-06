package com.kubsu.timetable.presentation.timetable.mapper

import com.kubsu.timetable.domain.entity.timetable.data.ClassEntity
import com.kubsu.timetable.presentation.timetable.model.ClassModel

object ClassModelMapper {
    fun toEntity(model: ClassModel): ClassEntity =
        ClassEntity(
            id = model.id,
            title = model.title,
            typeOfClass = TypeOfClassModelMapper.toEntity(model.typeOfClass),
            classroom = model.classroom,
            classTime = ClassTimeModelMapper.toEntity(model.classTime),
            day = model.day,
            lecturer = LecturerModelMapper.toEntity(model.lecturer),
            timetableId = model.timetableId,
            needToEmphasize = model.needToEmphasize
        )

    fun toModel(entity: ClassEntity): ClassModel =
        ClassModel(
            id = entity.id,
            title = entity.title,
            typeOfClass = TypeOfClassModelMapper.toModel(entity.typeOfClass),
            classroom = entity.classroom,
            classTime = ClassTimeModelMapper.toModel(entity.classTime),
            day = entity.day,
            lecturer = LecturerModelMapper.toModel(entity.lecturer),
            timetableId = entity.timetableId,
            needToEmphasize = entity.needToEmphasize
        )
}