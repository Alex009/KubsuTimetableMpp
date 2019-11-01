package com.kubsu.timetable.extensions

import com.kubsu.timetable.presentation.timetable.model.TimetableInfoToDisplay
import com.soywiz.klock.DateTime
import com.soywiz.klock.DayOfWeek
import com.soywiz.klock.weekOfYear1

fun getCurrentDayOfWeek(): DayOfWeek =
    DateTime.nowLocal().dayOfWeek

fun getWeekNumber(): Int =
    DateTime.nowLocal().weekOfYear1

fun List<TimetableInfoToDisplay>.indexOfNearestDayOrNull(): Int? {
    val currentDay = getCurrentDayOfWeek()
    return indexOfFirstOrNull {
        val dayOfWeek = (it as? TimetableInfoToDisplay.Day)?.dayOfWeek
            ?: return@indexOfFirstOrNull false
        dayOfWeek.displayDayIndex >= currentDay.displayDayIndex
    } ?: indexOfFirstOrNull { it is TimetableInfoToDisplay.Day }
}

val DayOfWeek.displayDayIndex: Int
    inline get() = index0Monday