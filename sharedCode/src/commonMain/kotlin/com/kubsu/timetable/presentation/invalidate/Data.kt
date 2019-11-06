package com.kubsu.timetable.presentation.invalidate

import com.kubsu.timetable.DataFailure
import platform.SerializableModel
import platform.SerializeModel

sealed class Action {
    object Invalidate : Action()

    internal object Success : Action()
    internal class Failure(val failure: DataFailure) : Action()
}

@SerializeModel
data class State(
    val progress: Boolean
) : SerializableModel

sealed class SideEffect {
    object Invalidate : SideEffect()
}

sealed class Subscription {
    class Navigate(val screen: Screen) : Subscription()

    class ShowFailure(val failure: DataFailure) : Subscription()
}

sealed class Screen {
    object Timetable : Screen()
}
