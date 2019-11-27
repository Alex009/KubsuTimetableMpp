package com.kubsu.timetable.fragments.bottomnav.settings

import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.egroden.teaco.*
import com.kubsu.timetable.BaseNavGraphDirections
import com.kubsu.timetable.R
import com.kubsu.timetable.fragments.bottomnav.BottomNavFragmentDirections
import com.kubsu.timetable.presentation.settings.Settings
import com.kubsu.timetable.utils.logics.DarkThemeStatus
import com.kubsu.timetable.utils.safeNavigate
import com.kubsu.timetable.utils.ui.materialAlert

class SettingsFragment(
    featureFactory: (
        oldState: Settings.State?
    ) -> Feature<Settings.Action, Settings.SideEffect, Settings.State, Settings.Subscription>
) : PreferenceFragmentCompat(),
    Render<Settings.State, Settings.Subscription> {
    private val connector by androidConnectors(featureFactory)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(this, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPreference<SwitchPreferenceCompat>("dark_theme")?.apply {
            setDefaultValue(DarkThemeStatus.getCacheStatus(view.context))
            setOnPreferenceClickListener {
                DarkThemeStatus.applyNewTheme(view.context, isChecked)
                true
            }
        }
        findPreference<Preference>("invalidation")?.apply {
            setOnPreferenceClickListener {
                connector bindAction Settings.Action.Invalidate
                true
            }
        }
        findPreference<Preference>("logout")?.apply {
            setOnPreferenceClickListener {
                requireActivity().materialAlert(
                    message = getString(R.string.are_you_sure_you_want_to_logout),
                    onOkButtonClick = { connector bindAction Settings.Action.Logout },
                    onNoButtonClick = {}
                )
                true
            }
        }
    }

    override fun renderState(state: Settings.State) = Unit

    override fun renderSubscription(subscription: Settings.Subscription) =
        when (subscription) {
            is Settings.Subscription.Navigate -> navigate(subscription.screen)
        }

    private fun navigate(screen: Settings.Screen) =
        safeNavigate(
            when (screen) {
                Settings.Screen.SignIn ->
                    BaseNavGraphDirections.actionGlobalSignInFragment()
                Settings.Screen.Invalidate ->
                    BottomNavFragmentDirections.actionBottomNavFragmentToInvalidateFragment()
            }
        )
}
