package com.kubsu.timetable.presentation.timetable.mapper

import com.soywiz.klock.DayOfWeek

object DayModelMapper {
    fun toEntity(
        value: Int
    ): DayOfWeek =
        if (value == 6) DayOfWeek[0] else DayOfWeek[value + 1]

    fun value(
        entity: DayOfWeek
    ): Int =
        entity.index0Monday
}