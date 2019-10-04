package com.kubsu.timetable.data.mapper.diff

import com.kubsu.timetable.domain.entity.diff.Basename

object BasenameMapper {
    private const val subscriptionValue = "subscription"
    private const val timetableValue = "timetable"
    private const val lecturerValue = "lecturer"
    private const val classValue = "class"

    fun toEntity(value: String): Basename =
        when (value) {
            subscriptionValue -> Basename.Subscription
            timetableValue -> Basename.Timetable
            lecturerValue -> Basename.Lecturer
            classValue -> Basename.Class
            else -> throw IllegalArgumentException("Unknown value: $value")
        }

    fun value(entity: Basename): String =
        when (entity) {
            Basename.Subscription -> subscriptionValue
            Basename.Timetable -> timetableValue
            Basename.Lecturer -> lecturerValue
            Basename.Class -> classValue
        }
}