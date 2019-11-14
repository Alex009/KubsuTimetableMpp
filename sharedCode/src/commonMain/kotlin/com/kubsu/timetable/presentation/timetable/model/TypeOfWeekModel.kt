package com.kubsu.timetable.presentation.timetable.model

import com.kubsu.timetable.platform.SerializableModel
import com.kubsu.timetable.platform.SerializeModel

sealed class TypeOfWeekModel : SerializableModel {
    @SerializeModel
    object Numerator : TypeOfWeekModel()

    @SerializeModel
    object Denominator : TypeOfWeekModel()
}