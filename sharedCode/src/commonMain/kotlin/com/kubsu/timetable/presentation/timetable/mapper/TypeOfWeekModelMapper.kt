package com.kubsu.timetable.presentation.timetable.mapper

import com.kubsu.timetable.domain.entity.timetable.data.TypeOfWeek
import com.kubsu.timetable.presentation.timetable.model.TypeOfWeekModel

object TypeOfWeekModelMapper {
    fun toEntity(model: TypeOfWeekModel): TypeOfWeek =
        when (model) {
            TypeOfWeekModel.Numerator -> TypeOfWeek.Numerator
            TypeOfWeekModel.Denominator -> TypeOfWeek.Denominator
        }

    fun toModel(entity: TypeOfWeek): TypeOfWeekModel =
        when (entity) {
            TypeOfWeek.Numerator -> TypeOfWeekModel.Numerator
            TypeOfWeek.Denominator -> TypeOfWeekModel.Denominator
        }
}