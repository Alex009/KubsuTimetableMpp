package com.kubsu.timetable.domain.entity.diff

sealed class Basename {
    object Subscription : Basename()
    object Timetable : Basename()
    object Lecturer : Basename()
    object Class : Basename()
    object MainInfo : Basename()
}