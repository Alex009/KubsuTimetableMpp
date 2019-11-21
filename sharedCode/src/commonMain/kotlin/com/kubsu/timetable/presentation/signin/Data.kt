package com.kubsu.timetable.presentation.signin

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

sealed class Action {
    class SignIn(val email: String, val password: String) : Action()
    object Registration : Action()

    internal object ShowResult : Action()
    internal class ShowSignInFailure(val failureList: List<SignInFail>) : Action()
    internal class ShowDataFailure(val failureList: List<DataFailure>) : Action()
}

@Parcelize
data class State(
    val progress: Boolean
) : Parcelable

sealed class SideEffect {
    class Authenticate(val email: String, val password: String) : SideEffect()
}

sealed class Subscription {
    class Navigate(val screen: Screen) : Subscription()

    class ShowDataFailure(val failureList: List<DataFailure>) : Subscription()
    class ShowSignInFailure(val failureList: List<SignInFail>) : Subscription()
}

sealed class Screen {
    object Registration : Screen()
    object Timetable : Screen()
}