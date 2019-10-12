package com.kubsu.timetable.presentation.timetable.model

import kotlinx.serialization.Serializable

@Serializable
sealed class TypeOfWeekModel {
    object Numerator : TypeOfWeekModel()
    object Denominator : TypeOfWeekModel()
}