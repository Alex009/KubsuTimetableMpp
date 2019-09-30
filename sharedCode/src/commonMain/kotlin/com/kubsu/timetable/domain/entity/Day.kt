package com.kubsu.timetable.domain.entity

sealed class Day {
    object Monday : Day()
    object Tuesday : Day()
    object Wednesday : Day()
    object Thursday : Day()
    object Friday : Day()
    object Saturday : Day()
    object Sunday : Day()

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
    }
}