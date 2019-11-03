package com.kubsu.timetable.domain.entity

sealed class Basename {
    object Subscription : Basename()
    object Timetable : Basename()
    object Lecturer : Basename()
    object Class : Basename()
    object UniversityInfo : Basename()
    object ClassTime : Basename()

    companion object {
        val list: List<Basename>
            get() = listOf(
                Subscription,
                Timetable,
                Lecturer,
                Class,
                UniversityInfo,
                ClassTime
            )
    }
}