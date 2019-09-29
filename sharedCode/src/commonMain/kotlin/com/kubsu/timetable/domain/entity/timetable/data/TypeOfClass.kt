package com.kubsu.timetable.domain.entity.timetable.data

sealed class TypeOfClass(internal val number: Int) {
    object Lecture : TypeOfClass(0)
    object Practice : TypeOfClass(1)

    companion object {
        fun convert(value: Int): TypeOfClass =
            when (value) {
                Lecture.number -> Lecture
                Practice.number -> Practice
                else -> throw IllegalArgumentException("Unknown argument: $value")
            }
    }
}
