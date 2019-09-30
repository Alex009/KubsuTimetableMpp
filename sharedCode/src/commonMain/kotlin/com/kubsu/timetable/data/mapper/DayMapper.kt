package com.kubsu.timetable.data.mapper

import com.kubsu.timetable.domain.entity.Day

object DayMapper {
    private const val mondayValue = 1
    private const val tuesdayValue = 2
    private const val wednesdayValue = 3
    private const val thursdayValue = 4
    private const val fridayValue = 5
    private const val saturdayValue = 6
    private const val sundayValue = 7

    fun toEntity(value: Int): Day =
        when (value) {
            mondayValue -> Day.Monday
            tuesdayValue -> Day.Tuesday
            wednesdayValue -> Day.Wednesday
            thursdayValue -> Day.Thursday
            fridayValue -> Day.Friday
            saturdayValue -> Day.Saturday
            sundayValue -> Day.Sunday
            else -> throw IllegalArgumentException("Unknown value: $value")
        }

    fun value(entity: Day): Int =
        when (entity) {
            Day.Monday -> mondayValue
            Day.Tuesday -> tuesdayValue
            Day.Wednesday -> wednesdayValue
            Day.Thursday -> thursdayValue
            Day.Friday -> fridayValue
            Day.Saturday -> saturdayValue
            Day.Sunday -> sundayValue
        }
}