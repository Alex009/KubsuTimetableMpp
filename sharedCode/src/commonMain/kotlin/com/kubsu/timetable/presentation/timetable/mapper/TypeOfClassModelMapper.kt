package com.kubsu.timetable.presentation.timetable.mapper

import com.kubsu.timetable.domain.entity.timetable.data.TypeOfClass
import com.kubsu.timetable.presentation.timetable.model.TypeOfClassModel

object TypeOfClassModelMapper {
    fun toEntity(model: TypeOfClassModel): TypeOfClass =
        when (model) {
            TypeOfClassModel.Lecture -> TypeOfClass.Lecture
            TypeOfClassModel.Practice -> TypeOfClass.Practice
        }

    fun toModel(entity: TypeOfClass): TypeOfClassModel =
        when (entity) {
            TypeOfClass.Lecture -> TypeOfClassModel.Lecture
            TypeOfClass.Practice -> TypeOfClassModel.Practice
        }
}