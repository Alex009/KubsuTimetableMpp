package com.kubsu.timetable.presentation.registration

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

sealed class Registration {
    sealed class Action : Registration() {
        class Registration(
            val email: String,
            val password: String,
            val repeatedPassword: String
        ) : Action()

        internal object ShowResult : Action()
        internal class ShowRegistrationFailure(
            val failureList: List<UserInfoFail>
        ) : Action()

        internal class ShowDataFailure(
            val failureList: List<DataFailure>
        ) : Action()
    }

    @Parcelize
    data class State(
        val progress: Boolean
    ) : Registration(), Parcelable

    sealed class SideEffect : Registration() {
        data class Registration(
            val email: String,
            val password: String
        ) : SideEffect()
    }

    sealed class Subscription : Registration() {
        class Navigate(val screen: Screen) : Subscription()

        class ShowRegistrationFailure(val failureList: List<UserInfoFail>) : Subscription()
        class ShowDataFailure(val failureList: List<DataFailure>) : Subscription()

        object PasswordsVary : Subscription()
    }

    sealed class Screen : Registration() {
        object CreateSubscription : Screen()
    }
}
