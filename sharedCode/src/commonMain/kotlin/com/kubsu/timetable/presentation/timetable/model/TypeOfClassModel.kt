package com.kubsu.timetable.presentation.timetable.model

import platform.SerializableModel
import platform.SerializeModel

sealed class TypeOfClassModel : SerializableModel {
    @SerializeModel
    object Lecture : TypeOfClassModel()

    @SerializeModel
    object Practice : TypeOfClassModel()
}