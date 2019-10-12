package com.kubsu.timetable.presentation.subscription.mapper

import com.kubsu.timetable.domain.entity.timetable.select.OccupationEntity
import com.kubsu.timetable.presentation.subscription.model.OccupationModel

object OccupationModelMapper {
    fun toModel(entity: OccupationEntity): OccupationModel =
        OccupationModel(
            id = entity.id,
            title = entity.title,
            code = entity.code,
            facultyId = entity.facultyId
        )

    fun toEntity(model: OccupationModel): OccupationEntity =
        OccupationEntity(
            id = model.id,
            title = model.title,
            code = model.code,
            facultyId = model.facultyId
        )
}