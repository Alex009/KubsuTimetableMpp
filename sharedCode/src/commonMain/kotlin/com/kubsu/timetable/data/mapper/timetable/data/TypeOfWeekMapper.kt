package com.kubsu.timetable.data.mapper.timetable.data

import com.kubsu.timetable.domain.entity.timetable.data.TypeOfWeek

object TypeOfWeekMapper {
    private const val numeratorValue = 0
    private const val denominatorValue = 1

    fun toEntity(value: Int): TypeOfWeek =
        when (value) {
            numeratorValue -> TypeOfWeek.Numerator
            denominatorValue -> TypeOfWeek.Denominator
            else -> throw IllegalArgumentException("Unknown value: $value")
        }

    fun value(entity: TypeOfWeek): Int =
        when (entity) {
            TypeOfWeek.Numerator -> numeratorValue
            TypeOfWeek.Denominator -> denominatorValue
        }
}