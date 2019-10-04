package com.kubsu.timetable.domain.entity

import com.soywiz.klock.DayOfWeek
import com.soywiz.klock.KlockLocale

object RussianKlockLocale : KlockLocale() {
    override val ISO639_1: String = "ru"
    override val firstDayOfWeek: DayOfWeek = DayOfWeek.Monday
    override val daysOfWeek: List<String> = listOf(
        "понедельник", "вторник", "среда", "четверг", "пятница", "суббота", "воскресенье"
    )
    override val months: List<String> = listOf(
        "январь", "февраль", "март", "апрель", "май", "июнь",
        "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"
    )
}