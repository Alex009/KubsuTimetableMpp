package com.kubsu.timetable.fragments.splash

import android.os.Bundle
import com.egroden.teaco.TeaFeature
import com.egroden.teaco.androidConnectors
import com.egroden.teaco.bindAction
import com.egroden.teaco.connect
import com.kubsu.timetable.BaseNavGraphDirections
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.splash.*
import com.kubsu.timetable.utils.safeNavigate

class SplashFragment(
    teaFeature: TeaFeature<Action, SideEffect, State, Subscription>
) : BaseFragment(R.layout.splash_fragment) {
    private val connector by androidConnectors(teaFeature) { bindAction(Action.Initiate) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(::render, ::render, lifecycle)
    }

    private fun render(state: State) = Unit

    private fun render(subscription: Subscription) =
        when (subscription) {
            is Subscription.Navigate -> navigation(subscription.screen)
            is Subscription.ShowFailure -> notifyUserOfFailure(subscription.failure)
        }

    private fun navigation(screen: Screen) =
        safeNavigate(
            when (screen) {
                Screen.SignInScreen ->
                    BaseNavGraphDirections.actionGlobalSignInFragment()
                Screen.TimetableScreen ->
                    SplashFragmentDirections.actionSplashFragmentToBottomNavFragment()
            }
        )
}
