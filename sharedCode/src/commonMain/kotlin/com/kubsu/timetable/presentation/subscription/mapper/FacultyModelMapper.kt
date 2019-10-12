package com.kubsu.timetable.presentation.subscription.mapper

import com.kubsu.timetable.domain.entity.timetable.select.FacultyEntity
import com.kubsu.timetable.presentation.subscription.model.FacultyModel

object FacultyModelMapper {
    fun toModel(entity: FacultyEntity): FacultyModel =
        FacultyModel(
            id = entity.id,
            title = entity.title
        )

    fun toEntity(model: FacultyModel): FacultyEntity =
        FacultyEntity(
            id = model.id,
            title = model.title
        )
}