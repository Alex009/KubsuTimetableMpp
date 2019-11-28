package com.kubsu.timetable.presentation.splash

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val splashUpdater =
    object : Updater<SplashState, SplashAction, SplashSubscription, SplashSideEffect> {
        override fun invoke(
            state: SplashState,
            action: SplashAction
        ): UpdateResponse<SplashState, SplashSubscription, SplashSideEffect> =
            when (action) {
                SplashAction.Initiate ->
                    UpdateResponse(
                        state,
                        sideEffects = setOf(SplashSideEffect.Initiate)
                    )

                SplashAction.ShowSignInScreen ->
                    UpdateResponse(
                        state,
                        subscription = SplashSubscription.Navigate(SplashScreen.SignInScreen)
                    )

                SplashAction.ShowTimetableScreen ->
                    UpdateResponse(
                        state,
                        subscription = SplashSubscription.Navigate(SplashScreen.TimetableScreen)
                    )

                is SplashAction.ShowFailure ->
                    UpdateResponse(
                        state,
                        subscription = SplashSubscription.ShowFailure(action.failure)
                    )
            }
}