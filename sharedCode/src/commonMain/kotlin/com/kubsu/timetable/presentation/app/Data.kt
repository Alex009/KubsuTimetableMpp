package com.kubsu.timetable.presentation.app

import platform.SerializableModel
import platform.SerializeModel

sealed class Action {
    object Start : Action()
}

@SerializeModel
class State : SerializableModel

sealed class SideEffect {
    object Initiate : SideEffect()
}

class Subscription