package com.kubsu.timetable.presentation.invalidate

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

sealed class InvidateAction {
    object Invalidate : InvidateAction()

    internal object Success : InvidateAction()
    internal class Failure(val failure: DataFailure) : InvidateAction()
}

@Parcelize
data class InvidateState(
    val progress: Boolean
) : Parcelable

sealed class InvidateSideEffect {
    object Invalidate : InvidateSideEffect()
}

sealed class InvidateSubscription {
    class Navigate(val screen: InvidateScreen) : InvidateSubscription()

    class ShowFailure(val failure: DataFailure) : InvidateSubscription()
}

sealed class InvidateScreen {
    object Timetable : InvidateScreen()
}