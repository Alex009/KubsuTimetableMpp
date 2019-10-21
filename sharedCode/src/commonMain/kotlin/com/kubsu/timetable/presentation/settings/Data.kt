package com.kubsu.timetable.presentation.settings

import platform.SerializableModel
import platform.SerializeModel

sealed class Action {
    object Logout : Action()

    internal object SuccessLogout : Action()
}

@SerializeModel
class State : SerializableModel

sealed class SideEffect {
    object Logout : SideEffect()
}

sealed class Subscription {
    class Navigate(val screen: Screen) : Subscription()
}

sealed class Screen {
    object SignIn : Screen()
}