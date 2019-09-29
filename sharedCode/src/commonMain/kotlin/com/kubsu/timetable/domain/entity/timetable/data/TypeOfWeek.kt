package com.kubsu.timetable.domain.entity.timetable.data

sealed class TypeOfWeek(val number: Int) {
    object Numerator : TypeOfWeek(0)
    object Denominator : TypeOfWeek(1)

    companion object {
        fun convert(value: Int) =
            when (value) {
                Numerator.number -> Numerator
                Denominator.number -> Denominator
                else -> throw IllegalArgumentException("Unknown argument: $value")
            }
    }
}