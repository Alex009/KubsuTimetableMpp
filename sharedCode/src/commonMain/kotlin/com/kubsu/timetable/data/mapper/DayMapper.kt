package com.kubsu.timetable.data.mapper

import com.kubsu.timetable.domain.entity.RussianKlockLocale
import com.soywiz.klock.DayOfWeek
import com.soywiz.klock.KlockLocale

object DayMapper {
    fun toEntity(
        value: Int,
        klockLocale: KlockLocale = RussianKlockLocale
    ): DayOfWeek =
        when (val firstDay = klockLocale.firstDayOfWeek) {
            DayOfWeek.Monday -> if (value == 7) DayOfWeek[0] else DayOfWeek[value]
            DayOfWeek.Sunday -> DayOfWeek[value - 1]
            else -> throw IllegalStateException("No implementation for the first day: $firstDay")
        }

    fun value(
        entity: DayOfWeek,
        klockLocale: KlockLocale = RussianKlockLocale
    ): Int =
        when (val firstDay = klockLocale.firstDayOfWeek) {
            DayOfWeek.Monday -> entity.index1Monday
            DayOfWeek.Sunday -> entity.index1Sunday
            else -> throw IllegalStateException("No implementation for the first day: $firstDay")
        }
}