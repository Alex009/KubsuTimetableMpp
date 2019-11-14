package com.kubsu.timetable.presentation.timetable.model

import com.kubsu.timetable.platform.SerializableModel
import com.kubsu.timetable.platform.SerializeModel

sealed class TypeOfClassModel : SerializableModel {
    @SerializeModel
    object Lecture : TypeOfClassModel()

    @SerializeModel
    object Practice : TypeOfClassModel()
}