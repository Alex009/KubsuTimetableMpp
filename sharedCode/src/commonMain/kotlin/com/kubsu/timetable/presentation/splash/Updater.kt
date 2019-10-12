package com.kubsu.timetable.presentation.splash

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val splashUpdater: Updater<State, Action, Subscription, SideEffect> = { state, action ->
    when (action) {
        Action.Initiate ->
            UpdateResponse(
                state,
                sideEffects = setOf(SideEffect.Initiate)
            )

        Action.ShowSignInScreen ->
            UpdateResponse(
                state,
                subscription = Subscription.Navigate(Screen.SignInScreen)
            )

        Action.ShowTimetableScreen ->
            UpdateResponse(
                state,
                subscription = Subscription.Navigate(Screen.TimetableScreen)
            )
    }
}