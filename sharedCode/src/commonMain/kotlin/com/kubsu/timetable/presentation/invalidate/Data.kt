package com.kubsu.timetable.presentation.invalidate

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

sealed class Invidate {
    sealed class Action : Invidate() {
        object Invalidate : Action()

        internal object Success : Action()
        internal class Failure(val failure: DataFailure) : Action()
    }

    @Parcelize
    data class State(
        val progress: Boolean
    ) : Invidate(), Parcelable

    sealed class SideEffect : Invidate() {
        object Invalidate : SideEffect()
    }

    sealed class Subscription : Invidate() {
        class Navigate(val screen: Screen) : Subscription()

        class ShowFailure(val failure: DataFailure) : Subscription()
    }

    sealed class Screen : Invidate() {
        object Timetable : Screen()
    }
}
