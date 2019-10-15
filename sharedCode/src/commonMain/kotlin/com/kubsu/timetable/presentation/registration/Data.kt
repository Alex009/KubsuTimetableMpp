package com.kubsu.timetable.presentation.registration

import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.RegistrationFail
import kotlinx.serialization.Serializable

sealed class Action {
    class Registration(val email: String, val password: String) : Action()

    internal object ShowResult : Action()
    internal class ShowFailure(
        val failure: RequestFailure<List<RegistrationFail>>
    ) : Action()
}

@Serializable
data class State(
    val progress: Boolean = false
)

sealed class SideEffect {
    data class Registration(val email: String, val password: String) : SideEffect()
}

sealed class Subscription {
    class Navigate(val screen: Screen) : Subscription()

    class ShowFailure(
        val failure: RequestFailure<List<RegistrationFail>>
    ) : Subscription()
}

sealed class Screen {
    object SignIn : Screen()
}