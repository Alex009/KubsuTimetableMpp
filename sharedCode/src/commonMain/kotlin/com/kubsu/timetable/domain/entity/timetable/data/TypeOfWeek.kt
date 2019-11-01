package com.kubsu.timetable.domain.entity.timetable.data

sealed class TypeOfWeek {
    object Numerator : TypeOfWeek()
    object Denominator : TypeOfWeek()

    companion object {
        val list: List<TypeOfWeek>
            get() = listOf(Numerator, Denominator)
    }
}