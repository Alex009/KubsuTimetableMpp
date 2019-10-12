package com.kubsu.timetable.presentation.signin

import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SignInFail
import kotlinx.serialization.Serializable

sealed class Action {
    class SignIn(val email: String, val password: String) : Action()
    object Registration : Action()

    internal object ShowResult : Action()
    internal class ShowError(val failure: RequestFailure<List<SignInFail>>) : Action()
}

@Serializable
data class State(
    val inProgress: Boolean = false
)

sealed class SideEffect {
    class Authenticate(val email: String, val password: String) : SideEffect()
}

sealed class Subscription {
    class Navigate(val screen: Screen) : Subscription()

    class ShowFailure(
        val failure: RequestFailure<List<SignInFail>>
    ) : Subscription()
}

sealed class Screen {
    object Registration : Screen()
    object Timetable : Screen()
}