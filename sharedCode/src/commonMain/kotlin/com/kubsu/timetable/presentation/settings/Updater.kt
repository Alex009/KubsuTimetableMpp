package com.kubsu.timetable.presentation.settings

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val settingsUpdater =
    object : Updater<Settings.State, Settings.Action, Settings.Subscription, Settings.SideEffect> {
        override fun invoke(
            state: Settings.State,
            action: Settings.Action
        ): UpdateResponse<Settings.State, Settings.Subscription, Settings.SideEffect> =
            when (action) {
                Settings.Action.Invalidate ->
                    UpdateResponse(
                        state,
                        subscription = Settings.Subscription.Navigate(Settings.Screen.Invalidate)
                    )

                Settings.Action.Logout ->
                    UpdateResponse(
                        state,
                        sideEffects = setOf(Settings.SideEffect.Logout)
                    )

                Settings.Action.SuccessLogout ->
                    UpdateResponse(
                        state,
                        subscription = Settings.Subscription.Navigate(Settings.Screen.SignIn)
                    )
            }
}
