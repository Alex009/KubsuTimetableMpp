package com.kubsu.timetable.presentation.timetable.mapper

import com.kubsu.timetable.domain.entity.timetable.data.ClassTimeEntity
import com.kubsu.timetable.presentation.timetable.model.ClassTimeModel

object ClassTimeModelMapper {
    fun toEntity(model: ClassTimeModel): ClassTimeEntity =
        ClassTimeEntity(
            id = model.id,
            number = model.number,
            start = model.start,
            end = model.end
        )

    fun toModel(entity: ClassTimeEntity): ClassTimeModel =
        ClassTimeModel(
            id = entity.id,
            number = entity.number,
            start = entity.start,
            end = entity.end
        )
}