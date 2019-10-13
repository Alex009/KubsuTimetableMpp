package com.kubsu.timetable.presentation.timetable

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel
import com.kubsu.timetable.presentation.timetable.model.TimetableModel
import com.kubsu.timetable.presentation.timetable.model.UniversityInfoModel
import kotlinx.serialization.Serializable

sealed class Action {
    class UpdateData(val subscription: SubscriptionModel) : Action()

    internal class ShowTimetable(
        val universityInfoModel: UniversityInfoModel?,
        val numeratorTimetable: TimetableModel?,
        val denominatorTimetable: TimetableModel?
    ) : Action()

    internal class ShowFailure(
        val failure: DataFailure
    ) : Action()
}

@Serializable
data class State(
    val progress: Boolean,
    val currentSubscription: SubscriptionModel,
    val universityInfoModel: UniversityInfoModel?,
    val numeratorTimetable: TimetableModel?,
    val denominatorTimetable: TimetableModel?
)

sealed class SideEffect {
    class LoadCurrentTimetable(val subscription: SubscriptionModel) : SideEffect()
}

sealed class Subscription {
    class Navigate(val screen: Screen) : Subscription()

    class ShowFailure(val failure: DataFailure) : Subscription()
}

sealed class Screen