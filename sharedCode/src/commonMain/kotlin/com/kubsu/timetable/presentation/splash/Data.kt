package com.kubsu.timetable.presentation.splash

import com.kubsu.timetable.DataFailure
import platform.SerializableModel
import platform.SerializeModel

sealed class Action {
    object Initiate : Action()

    internal object ShowTimetableScreen : Action()
    internal object ShowSignInScreen : Action()

    internal class ShowFailure(val failure: DataFailure) : Action()
}

@SerializeModel
class State : SerializableModel

sealed class SideEffect {
    object Initiate : SideEffect()
}

sealed class Subscription {
    class Navigate(val screen: Screen) : Subscription()
    class ShowFailure(val failure: DataFailure) : Subscription()
}

sealed class Screen {
    object SignInScreen : Screen()

    object TimetableScreen : Screen()
}