package com.kubsu.timetable.data.mapper.diff

import com.kubsu.timetable.domain.entity.Basename

object BasenameMapper {
    private const val subscriptionValue = "subscriptions"
    private const val timetableValue = "timetables"
    private const val lecturerValue = "lecturers"
    private const val classValue = "classes"
    private const val universityInfoValue = "university_info"

    fun toEntity(value: String): Basename =
        when (value) {
            subscriptionValue -> Basename.Subscription
            timetableValue -> Basename.Timetable
            lecturerValue -> Basename.Lecturer
            classValue -> Basename.Class
            universityInfoValue -> Basename.UniversityInfo
            else -> throw IllegalArgumentException("Unknown value: $value")
        }

    fun value(entity: Basename): String =
        when (entity) {
            Basename.Subscription -> subscriptionValue
            Basename.Timetable -> timetableValue
            Basename.Lecturer -> lecturerValue
            Basename.Class -> classValue
            Basename.UniversityInfo -> universityInfoValue
        }
}