package com.kubsu.timetable.presentation.registration

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

sealed class RegistrationAction {
    class Registration(
        val email: String,
        val password: String,
        val repeatedPassword: String
    ) : RegistrationAction()

    internal object ShowResult : RegistrationAction()
    internal class ShowRegistrationFailure(
        val failureList: List<UserInfoFail>
    ) : RegistrationAction()

    internal class ShowDataFailure(
        val failureList: List<DataFailure>
    ) : RegistrationAction()
}

@Parcelize
data class RegistrationState(
    val progress: Boolean
) : Parcelable

sealed class RegistrationSideEffect {
    data class Registration(
        val email: String,
        val password: String
    ) : RegistrationSideEffect()
}

sealed class RegistrationSubscription {
    class Navigate(val screen: RegistrationScreen) : RegistrationSubscription()

    class ShowRegistrationFailure(val failureList: List<UserInfoFail>) : RegistrationSubscription()
    class ShowDataFailure(val failureList: List<DataFailure>) : RegistrationSubscription()

    object PasswordsVary : RegistrationSubscription()
}

sealed class RegistrationScreen {
    object CreateSubscription : RegistrationScreen()
}