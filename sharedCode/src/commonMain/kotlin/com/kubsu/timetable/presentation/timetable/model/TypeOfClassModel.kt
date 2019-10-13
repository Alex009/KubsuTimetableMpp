package com.kubsu.timetable.presentation.timetable.model

import kotlinx.serialization.Serializable

@Serializable
sealed class TypeOfClassModel {
    object Lecture : TypeOfClassModel()
    object Practice : TypeOfClassModel()
}
