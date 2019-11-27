package com.kubsu.timetable.presentation.splash

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

sealed class Splash {
    sealed class Action : Splash() {
        object Initiate : Action()

        internal object ShowTimetableScreen : Action()
        internal object ShowSignInScreen : Action()

        internal class ShowFailure(val failure: DataFailure) : Action()
    }

    @Parcelize
    object State : Splash(), Parcelable

    sealed class SideEffect : Splash() {
        object Initiate : SideEffect()
    }

    sealed class Subscription : Splash() {
        class Navigate(val screen: Screen) : Subscription()
        class ShowFailure(val failure: DataFailure) : Subscription()
    }

    sealed class Screen : Splash() {
        object SignInScreen : Screen()

        object TimetableScreen : Screen()
    }
}
