package com.kubsu.timetable.presentation.splash

import kotlinx.serialization.Serializable

sealed class Action {
    object Initiate : Action()

    internal object ShowTimetableScreen : Action()
    internal object ShowSignInScreen : Action()
}

@Serializable
class State

sealed class SideEffect {
    object Initiate : SideEffect()
}

sealed class Subscription {
    class Navigate(val screen: Screen) : Subscription()
}

sealed class Screen {
    object SignInScreen : Screen()

    object TimetableScreen : Screen()
}