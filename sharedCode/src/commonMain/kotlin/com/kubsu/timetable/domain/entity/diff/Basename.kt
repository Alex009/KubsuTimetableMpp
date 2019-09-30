package com.kubsu.timetable.domain.entity.diff

sealed class Basename {
    object Subscription : Basename()
    object Timetable : Basename()
    object Lecturer : Basename()
    object Class : Basename()
    object MainInfo : Basename()

    companion object {
        val list: List<Basename>
            get() = listOf(
                Subscription,
                Timetable,
                Lecturer,
                Class,
                MainInfo
            )
    }
}