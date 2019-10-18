package com.kubsu.timetable.presentation.registration

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RegistrationFail
import platform.SerializableModel
import platform.SerializeModel

sealed class Action {
    class Registration(val email: String, val password: String) : Action()

    internal object ShowResult : Action()
    internal class ShowRegistrationFailure(
        val failureList: List<RegistrationFail>
    ) : Action()

    internal class ShowDataFailure(
        val failureList: List<DataFailure>
    ) : Action()
}

@SerializeModel
data class State(
    val progress: Boolean = false
) : SerializableModel

sealed class SideEffect {
    data class Registration(val email: String, val password: String) : SideEffect()
}

sealed class Subscription {
    class Navigate(val screen: Screen) : Subscription()

    class ShowRegistrationFailure(val failureList: List<RegistrationFail>) : Subscription()
    class ShowDataFailure(val failureList: List<DataFailure>) : Subscription()
}

sealed class Screen {
    object SignIn : Screen()
}