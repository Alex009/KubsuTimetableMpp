package com.kubsu.timetable.data.mapper

import com.soywiz.klock.DayOfWeek

object DayDtoMapper {
    fun toEntity(
        value: Int
    ): DayOfWeek =
        if (value == 7) DayOfWeek[0] else DayOfWeek[value]

    fun value(
        entity: DayOfWeek
    ): Int =
        entity.index1Monday
}