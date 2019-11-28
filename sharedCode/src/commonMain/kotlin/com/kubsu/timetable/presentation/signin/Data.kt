package com.kubsu.timetable.presentation.signin

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

sealed class SignInAction {
    class SignIn(val email: String, val password: String) : SignInAction()
    object Registration : SignInAction()

    internal object ShowResult : SignInAction()
    internal class ShowSignInFailure(val failureList: List<SignInFail>) : SignInAction()
    internal class ShowDataFailure(val failureList: List<DataFailure>) : SignInAction()
}

@Parcelize
data class SignInState(
    val progress: Boolean
) : Parcelable

sealed class SignInSideEffect {
    class Authenticate(val email: String, val password: String) : SignInSideEffect()
}

sealed class SignInSubscription {
    class Navigate(val screen: SignInScreen) : SignInSubscription()

    class ShowDataFailure(val failureList: List<DataFailure>) : SignInSubscription()
    class ShowSignInFailure(val failureList: List<SignInFail>) : SignInSubscription()
}

sealed class SignInScreen {
    object Registration : SignInScreen()
    object Timetable : SignInScreen()
}