package com.kubsu.timetable.presentation.settings

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

sealed class Settings {
    sealed class Action : Settings() {
        object Invalidate : Action()

        object Logout : Action()

        internal object SuccessLogout : Action()
    }

    @Parcelize
    object State : Settings(), Parcelable

    sealed class SideEffect : Settings() {
        object Logout : SideEffect()
    }

    sealed class Subscription : Settings() {
        class Navigate(val screen: Screen) : Subscription()
    }

    sealed class Screen : Settings() {
        object SignIn : Screen()
        object Invalidate : Screen()
    }
}