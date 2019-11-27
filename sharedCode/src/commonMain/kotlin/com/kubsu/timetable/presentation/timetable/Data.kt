package com.kubsu.timetable.presentation.timetable

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize
import com.kubsu.timetable.presentation.timetable.model.ClassModel
import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel
import com.kubsu.timetable.presentation.timetable.model.TimetableModel
import com.kubsu.timetable.presentation.timetable.model.UniversityInfoModel

sealed class Timetable {
    sealed class Action : Timetable() {
        class LoadData(val subscription: SubscriptionModel?) : Action()

        class WasDisplayed(val classModel: ClassModel) : Action()

        internal class ShowTimetable(
            val universityInfoModel: UniversityInfoModel?,
            val numeratorTimetable: TimetableModel?,
            val denominatorTimetable: TimetableModel?
        ) : Action()
    }

    @Parcelize
    data class State(
        val progress: Boolean,
        val currentSubscription: SubscriptionModel?,
        val universityInfoModel: UniversityInfoModel?,
        val numeratorTimetable: TimetableModel?,
        val denominatorTimetable: TimetableModel?
    ) : Timetable(), Parcelable

    sealed class SideEffect : Timetable() {
        class LoadCurrentTimetable(val subscription: SubscriptionModel) : SideEffect()
        class ChangesWasDisplayed(val classModel: ClassModel) : SideEffect()
    }

    sealed class Subscription : Timetable() {
        class Navigate(val screen: Screen) : Subscription()
    }

    sealed class Screen : Timetable() {
        class NextWeekTimetable(
            val universityInfo: UniversityInfoModel?,
            val timetable: TimetableModel?
        ) : Screen()
    }
}
