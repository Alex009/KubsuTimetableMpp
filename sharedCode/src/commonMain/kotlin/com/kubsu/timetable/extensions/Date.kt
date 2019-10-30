package com.kubsu.timetable.extensions

import com.kubsu.timetable.presentation.timetable.model.TimetableInfoToDisplay
import com.soywiz.klock.DateTime
import com.soywiz.klock.DayOfWeek

fun getCurrentDayOfWeek(): DayOfWeek =
    DateTime.nowLocal().dayOfWeek

fun List<TimetableInfoToDisplay>.indexOfNearestDayOrNull(): Int? {
    val currentDay = getCurrentDayOfWeek()
    return indexOfFirst {
        val dayOfWeek = (it as? TimetableInfoToDisplay.Day)?.dayOfWeek ?: return@indexOfFirst false
        dayOfWeek.displayDayIndex >= currentDay.displayDayIndex
    }.takeIf { it > -1 }
}

val DayOfWeek.displayDayIndex: Int
    inline get() = index0Monday