package com.kubsu.timetable.presentation.timetable

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize
import com.kubsu.timetable.presentation.timetable.model.ClassModel
import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel
import com.kubsu.timetable.presentation.timetable.model.TimetableModel
import com.kubsu.timetable.presentation.timetable.model.UniversityInfoModel

sealed class TimetableAction {
    class LoadData(val subscription: SubscriptionModel?) : TimetableAction()

    class WasDisplayed(val classModel: ClassModel) : TimetableAction()

    internal class ShowTimetable(
        val universityInfoModel: UniversityInfoModel?,
        val numeratorTimetable: TimetableModel?,
        val denominatorTimetable: TimetableModel?
    ) : TimetableAction()
}

@Parcelize
data class TimetableState(
    val progress: Boolean,
    val currentSubscription: SubscriptionModel?,
    val universityInfoModel: UniversityInfoModel?,
    val numeratorTimetable: TimetableModel?,
    val denominatorTimetable: TimetableModel?
) : Parcelable

sealed class TimetableSideEffect {
    class LoadCurrentTimetable(val subscription: SubscriptionModel) : TimetableSideEffect()
    class ChangesWasDisplayed(val classModel: ClassModel) : TimetableSideEffect()
}

sealed class TimetableSubscription {
    class Navigate(val screen: TimetableScreen) : TimetableSubscription()
}

sealed class TimetableScreen {
    class NextWeekTimetable(
        val universityInfo: UniversityInfoModel?,
        val timetable: TimetableModel?
    ) : TimetableScreen()
}