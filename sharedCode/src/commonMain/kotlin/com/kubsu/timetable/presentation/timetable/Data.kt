package com.kubsu.timetable.presentation.timetable

import com.kubsu.timetable.platform.SerializableModel
import com.kubsu.timetable.platform.SerializeModel
import com.kubsu.timetable.presentation.timetable.model.ClassModel
import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel
import com.kubsu.timetable.presentation.timetable.model.TimetableModel
import com.kubsu.timetable.presentation.timetable.model.UniversityInfoModel

sealed class Action {
    class LoadData(val subscription: SubscriptionModel?) : Action()

    class WasDisplayed(val classModel: ClassModel) : Action()

    internal class ShowTimetable(
        val universityInfoModel: UniversityInfoModel?,
        val numeratorTimetable: TimetableModel?,
        val denominatorTimetable: TimetableModel?
    ) : Action()
}

@SerializeModel
data class State(
    val progress: Boolean,
    val currentSubscription: SubscriptionModel?,
    val universityInfoModel: UniversityInfoModel?,
    val numeratorTimetable: TimetableModel?,
    val denominatorTimetable: TimetableModel?
) : SerializableModel

sealed class SideEffect {
    class LoadCurrentTimetable(val subscription: SubscriptionModel) : SideEffect()
    class ChangesWasDisplayed(val classModel: ClassModel) : SideEffect()
}

sealed class Subscription {
    class Navigate(val screen: Screen) : Subscription()
}

sealed class Screen {
    class NextWeekTimetable(
        val universityInfo: UniversityInfoModel?,
        val timetable: TimetableModel?
    ) : Screen()
}