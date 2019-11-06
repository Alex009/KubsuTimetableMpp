package com.kubsu.timetable.presentation.settings

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val settingsUpdater: Updater<State, Action, Subscription, SideEffect> = { state, action ->
    when (action) {
        Action.Invalidate ->
            UpdateResponse(
                state,
                subscription = Subscription.Navigate(Screen.Invalidate)
            )

        Action.Logout ->
            UpdateResponse(
                state,
                sideEffects = setOf(SideEffect.Logout)
            )

        Action.SuccessLogout ->
            UpdateResponse(
                state,
                subscription = Subscription.Navigate(Screen.SignIn)
            )
    }
}
