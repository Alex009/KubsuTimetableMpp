package com.kubsu.timetable.fragments.bottomnav.settings

import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.egroden.teaco.Feature
import com.egroden.teaco.androidConnectors
import com.egroden.teaco.bindAction
import com.egroden.teaco.connect
import com.kubsu.timetable.BaseNavGraphDirections
import com.kubsu.timetable.R
import com.kubsu.timetable.presentation.settings.*
import com.kubsu.timetable.utils.logics.DarkThemeStatus
import com.kubsu.timetable.utils.safeNavigate
import com.kubsu.timetable.utils.ui.materialAlert

class SettingsFragment(
    featureFactory: (oldState: State?) -> Feature<Action, SideEffect, State, Subscription>
) : PreferenceFragmentCompat() {
    private val connector by androidConnectors(featureFactory)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(::render, ::render, lifecycle)
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
        findPreference<Preference>("logout")?.apply {
            setOnPreferenceClickListener {
                requireActivity().materialAlert(
                    message = getString(R.string.are_you_sure_you_want_to_logout),
                    onOkButtonClick = { connector bindAction Action.Logout },
                    onNoButtonClick = {}
                )
                true
            }
        }
    }

    private fun render(state: State) = Unit

    private fun render(subscription: Subscription) =
        when (subscription) {
            is Subscription.Navigate -> navigate(subscription.screen)
        }

    private fun navigate(screen: Screen) =
        safeNavigate(
            when (screen) {
                Screen.SignIn ->
                    BaseNavGraphDirections.actionGlobalSignInFragment()
            }
        )
}
