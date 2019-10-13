package com.kubsu.timetable.presentation.timetable.mapper

import com.kubsu.timetable.domain.entity.timetable.data.LecturerEntity
import com.kubsu.timetable.presentation.timetable.model.LecturerModel

object LecturerModelMapper {
    fun toEntity(model: LecturerModel): LecturerEntity =
        LecturerEntity(
            id = model.id,
            name = model.name,
            surname = model.surname,
            patronymic = model.patronymic
        )

    fun toModel(entity: LecturerEntity): LecturerModel =
        LecturerModel(
            id = entity.id,
            name = entity.name,
            surname = entity.surname,
            patronymic = entity.patronymic
        )
}