package com.kubsu.timetable.presentation.splash

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val splashUpdater =
    object : Updater<Splash.State, Splash.Action, Splash.Subscription, Splash.SideEffect> {
        override fun invoke(
            state: Splash.State,
            action: Splash.Action
        ): UpdateResponse<Splash.State, Splash.Subscription, Splash.SideEffect> =
            when (action) {
                Splash.Action.Initiate ->
                    UpdateResponse(
                        state,
                        sideEffects = setOf(Splash.SideEffect.Initiate)
                    )

                Splash.Action.ShowSignInScreen ->
                    UpdateResponse(
                        state,
                        subscription = Splash.Subscription.Navigate(Splash.Screen.SignInScreen)
                    )

                Splash.Action.ShowTimetableScreen ->
                    UpdateResponse(
                        state,
                        subscription = Splash.Subscription.Navigate(Splash.Screen.TimetableScreen)
                    )

                is Splash.Action.ShowFailure ->
                    UpdateResponse(
                        state,
                        subscription = Splash.Subscription.ShowFailure(action.failure)
                    )
            }
}