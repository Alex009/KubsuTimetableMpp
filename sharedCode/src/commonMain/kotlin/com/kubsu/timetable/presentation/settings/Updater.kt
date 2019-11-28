package com.kubsu.timetable.presentation.settings

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val settingsUpdater =
    object : Updater<SettingsState, SettingsAction, SettingsSubscription, SettingsSideEffect> {
        override fun invoke(
            state: SettingsState,
            action: SettingsAction
        ): UpdateResponse<SettingsState, SettingsSubscription, SettingsSideEffect> =
            when (action) {
                SettingsAction.Invalidate ->
                    UpdateResponse(
                        state,
                        subscription = SettingsSubscription.Navigate(SettingsScreen.Invalidate)
                    )

                SettingsAction.Logout ->
                    UpdateResponse(
                        state,
                        sideEffects = setOf(SettingsSideEffect.Logout)
                    )

                SettingsAction.SuccessLogout ->
                    UpdateResponse(
                        state,
                        subscription = SettingsSubscription.Navigate(SettingsScreen.SignIn)
                    )
            }
}
