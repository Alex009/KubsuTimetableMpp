package com.kubsu.timetable.data.mapper.timetable.data

import com.kubsu.timetable.domain.entity.timetable.data.TypeOfClass

object TypeOfClassDtoMapper {
    private const val practiceValue = 0
    private const val lectureValue = 1

    fun toEntity(value: Int): TypeOfClass =
        when (value) {
            lectureValue -> TypeOfClass.Lecture
            practiceValue -> TypeOfClass.Practice
            else -> throw IllegalArgumentException("Unknown value: $value")
        }

    fun value(entity: TypeOfClass): Int =
        when (entity) {
            TypeOfClass.Lecture -> lectureValue
            TypeOfClass.Practice -> practiceValue
        }
}