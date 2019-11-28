package com.kubsu.timetable.fragments.splash

import android.os.Bundle
import com.egroden.teaco.*
import com.kubsu.timetable.BaseNavGraphDirections
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.splash.*
import com.kubsu.timetable.utils.safeNavigate

class SplashFragment(
    featureFactory: (
        oldState: SplashState?
    ) -> Feature<SplashAction, SplashSideEffect, SplashState, SplashSubscription>
) : BaseFragment(R.layout.splash_fragment),
    Render<SplashState, SplashSubscription> {
    private val connector by androidConnectors(featureFactory) { bindAction(SplashAction.Initiate) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(this, lifecycle)
    }

    override fun renderState(state: SplashState) = Unit

    override fun renderSubscription(subscription: SplashSubscription) =
        when (subscription) {
            is SplashSubscription.Navigate -> navigation(subscription.screen)
            is SplashSubscription.ShowFailure -> notifyUserOfFailure(subscription.failure)
        }

    private fun navigation(screen: SplashScreen) =
        safeNavigate(
            when (screen) {
                SplashScreen.SignInScreen ->
                    BaseNavGraphDirections.actionGlobalSignInFragment()
                SplashScreen.TimetableScreen ->
                    SplashFragmentDirections.actionSplashFragmentToBottomNavFragment()
            }
        )
}
