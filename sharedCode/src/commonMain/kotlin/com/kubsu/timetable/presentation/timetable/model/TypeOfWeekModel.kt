package com.kubsu.timetable.presentation.timetable.model

import platform.SerializableModel
import platform.SerializeModel

sealed class TypeOfWeekModel : SerializableModel {
    @SerializeModel
    object Numerator : TypeOfWeekModel()

    @SerializeModel
    object Denominator : TypeOfWeekModel()
}