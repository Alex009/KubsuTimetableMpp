package com.kubsu.timetable.presentation.splash

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

sealed class SplashAction {
    object Initiate : SplashAction()

    internal object ShowTimetableScreen : SplashAction()
    internal object ShowSignInScreen : SplashAction()

    internal class ShowFailure(val failure: DataFailure) : SplashAction()
}

@Parcelize
object SplashState : Parcelable

sealed class SplashSideEffect {
    object Initiate : SplashSideEffect()
}

sealed class SplashSubscription {
    class Navigate(val screen: SplashScreen) : SplashSubscription()
    class ShowFailure(val failure: DataFailure) : SplashSubscription()
}

sealed class SplashScreen {
    object SignInScreen : SplashScreen()

    object TimetableScreen : SplashScreen()
}
