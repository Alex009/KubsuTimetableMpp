package com.kubsu.timetable.presentation.settings

import com.kubsu.timetable.platform.Parcelable
import com.kubsu.timetable.platform.Parcelize

sealed class SettingsAction {
    object Invalidate : SettingsAction()

    object Logout : SettingsAction()

    internal object SuccessLogout : SettingsAction()
}

@Parcelize
object SettingsState : Parcelable

sealed class SettingsSideEffect {
    object Logout : SettingsSideEffect()
}

sealed class SettingsSubscription {
    class Navigate(val screen: SettingsScreen) : SettingsSubscription()
}

sealed class SettingsScreen {
    object SignIn : SettingsScreen()
    object Invalidate : SettingsScreen()
}