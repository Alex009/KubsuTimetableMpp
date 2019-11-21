package com.kubsu.timetable.presentation.settings

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

sealed class Action {
    object Invalidate : Action()

    object Logout : Action()

    internal object SuccessLogout : Action()
}

@Parcelize
class State : Parcelable

sealed class SideEffect {
    object Logout : SideEffect()
}

sealed class Subscription {
    class Navigate(val screen: Screen) : Subscription()
}

sealed class Screen {
    object SignIn : Screen()
    object Invalidate : Screen()
}