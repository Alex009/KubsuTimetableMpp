package com.kubsu.timetable.domain.entity.timetable.data

sealed class TypeOfWeek {
    object Numerator : TypeOfWeek()
    object Denominator : TypeOfWeek()
}