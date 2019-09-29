package com.kubsu.timetable.domain.entity

sealed class Day(val number: Int) {
    object Monday : Day(1)
    object Tuesday : Day(2)
    object Wednesday : Day(3)
    object Thursday : Day(4)
    object Friday : Day(5)
    object Saturday : Day(6)
    object Sunday : Day(7)

    companion object {
        val list: List<Day>
            get() = listOf(
                Monday,
                Tuesday,
                Wednesday,
                Thursday,
                Friday,
                Saturday,
                Sunday
            )

        fun getDay(number: Int) = when (number) {
            Monday.number -> Monday
            Tuesday.number -> Tuesday
            Wednesday.number -> Wednesday
            Thursday.number -> Thursday
            Friday.number -> Friday
            Saturday.number -> Saturday
            Sunday.number -> Sunday
            else -> throw IllegalArgumentException("Unknown day: $number")
        }
    }
}