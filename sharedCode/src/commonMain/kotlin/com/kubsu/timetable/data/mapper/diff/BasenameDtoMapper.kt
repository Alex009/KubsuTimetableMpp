package com.kubsu.timetable.data.mapper.diff

import com.kubsu.timetable.domain.entity.Basename

object BasenameDtoMapper {
    private const val subscriptionValue = "subscriptions"
    private const val timetableValue = "timetables"
    private const val lecturerValue = "lecturers"
    private const val classValue = "classes"
    private const val universityInfoValue = "university-info"
    private const val classTimeValue = "classtimes"

    fun toEntity(value: String): Basename =
        when (value) {
            subscriptionValue -> Basename.Subscription
            timetableValue -> Basename.Timetable
            lecturerValue -> Basename.Lecturer
            classValue -> Basename.Class
            universityInfoValue -> Basename.UniversityInfo
            classTimeValue -> Basename.ClassTime
            else -> throw IllegalArgumentException("Unknown value: $value")
        }

    fun value(entity: Basename): String =
        when (entity) {
            Basename.Subscription -> subscriptionValue
            Basename.Timetable -> timetableValue
            Basename.Lecturer -> lecturerValue
            Basename.Class -> classValue
            Basename.UniversityInfo -> universityInfoValue
            Basename.ClassTime -> classTimeValue
        }
}