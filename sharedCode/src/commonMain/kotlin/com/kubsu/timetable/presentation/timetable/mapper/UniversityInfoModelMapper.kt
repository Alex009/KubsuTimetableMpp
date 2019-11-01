package com.kubsu.timetable.presentation.timetable.mapper

import com.kubsu.timetable.domain.entity.timetable.data.UniversityInfoEntity
import com.kubsu.timetable.presentation.timetable.model.UniversityInfoModel

object UniversityInfoModelMapper {
    fun toEntity(model: UniversityInfoModel): UniversityInfoEntity =
        UniversityInfoEntity(
            id = model.id,
            facultyId = model.facultyId,
            typeOfWeek = TypeOfWeekModelMapper.toEntity(model.typeOfWeek),
            weekNumber = model.weekNumber
        )

    fun toModel(entity: UniversityInfoEntity): UniversityInfoModel =
        UniversityInfoModel(
            id = entity.id,
            facultyId = entity.facultyId,
            typeOfWeek = TypeOfWeekModelMapper.toModel(entity.typeOfWeek),
            weekNumber = entity.weekNumber
        )
}