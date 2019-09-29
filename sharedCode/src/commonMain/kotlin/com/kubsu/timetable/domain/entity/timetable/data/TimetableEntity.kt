package com.kubsu.timetable.domain.entity.timetable.data

data class TimetableEntity(
    val id: Int,
    val typeOfWeek: TypeOfWeek,
    val subgroupId: Int,
    val classList: List<ClassEntity>
)