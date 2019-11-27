package com.kubsu.timetable.presentation.signin

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

sealed class SignIn {
    sealed class Action : SignIn() {
        class SignIn(val email: String, val password: String) : Action()
        object Registration : Action()

        internal object ShowResult : Action()
        internal class ShowSignInFailure(val failureList: List<SignInFail>) : Action()
        internal class ShowDataFailure(val failureList: List<DataFailure>) : Action()
    }

    @Parcelize
    data class State(
        val progress: Boolean
    ) : SignIn(), Parcelable

    sealed class SideEffect : SignIn() {
        class Authenticate(val email: String, val password: String) : SideEffect()
    }

    sealed class Subscription : SignIn() {
        class Navigate(val screen: Screen) : Subscription()

        class ShowDataFailure(val failureList: List<DataFailure>) : Subscription()
        class ShowSignInFailure(val failureList: List<SignInFail>) : Subscription()
    }

    sealed class Screen : SignIn() {
        object Registration : Screen()
        object Timetable : Screen()
    }
}