package com.kubsu.timetable.domain.entity.timetable.data

sealed class TypeOfClass {
    object Lecture : TypeOfClass()
    object Practice : TypeOfClass()
}
