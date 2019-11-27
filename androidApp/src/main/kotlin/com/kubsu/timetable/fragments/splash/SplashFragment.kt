package com.kubsu.timetable.fragments.splash

import android.os.Bundle
import com.egroden.teaco.*
import com.kubsu.timetable.BaseNavGraphDirections
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.splash.Splash
import com.kubsu.timetable.utils.safeNavigate

class SplashFragment(
    featureFactory: (
        oldState: Splash.State?
    ) -> Feature<Splash.Action, Splash.SideEffect, Splash.State, Splash.Subscription>
) : BaseFragment(R.layout.splash_fragment),
    Render<Splash.State, Splash.Subscription> {
    private val connector by androidConnectors(featureFactory) { bindAction(Splash.Action.Initiate) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(this, lifecycle)
    }

    override fun renderState(state: Splash.State) = Unit

    override fun renderSubscription(subscription: Splash.Subscription) =
        when (subscription) {
            is Splash.Subscription.Navigate -> navigation(subscription.screen)
            is Splash.Subscription.ShowFailure -> notifyUserOfFailure(subscription.failure)
        }

    private fun navigation(screen: Splash.Screen) =
        safeNavigate(
            when (screen) {
                Splash.Screen.SignInScreen ->
                    BaseNavGraphDirections.actionGlobalSignInFragment()
                Splash.Screen.TimetableScreen ->
                    SplashFragmentDirections.actionSplashFragmentToBottomNavFragment()
            }
        )
}
