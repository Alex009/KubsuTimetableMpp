package com.kubsu.timetable.presentation.signin

import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SignInFail
import platform.SerializableModel
import platform.SerializeModel

sealed class Action {
    class SignIn(val email: String, val password: String) : Action()
    object Registration : Action()

    internal object ShowResult : Action()
    internal class ShowFailure(val failure: RequestFailure<List<SignInFail>>) : Action()
}

@SerializeModel
data class State(
    val progress: Boolean = false
) : SerializableModel

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