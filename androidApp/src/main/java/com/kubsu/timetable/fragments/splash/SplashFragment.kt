package com.kubsu.timetable.fragments.splash

import android.os.Bundle
import com.egroden.teaco.*
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.splash.*
import com.kubsu.timetable.utils.connect
import com.kubsu.timetable.utils.getNavControllerOrNull

class SplashFragment(
    teaFeature: TeaFeature<Action, SideEffect, State, Subscription>,
    stateParser: StateParser<State>
) : BaseFragment(R.layout.splash_fragment) {
    private val connector by androidConnectors(teaFeature, stateParser) {
        bindAction(Action.Initiate)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(::render, ::render)
    }

    private fun render(state: State) = Unit

    private fun render(subscription: Subscription) =
        when (subscription) {
            is Subscription.Navigate -> navigation(subscription.screen)
        }

    private fun navigation(screen: Screen) {
        getNavControllerOrNull()?.navigate(
            when (screen) {
                Screen.SignInScreen ->
                    SplashFragmentDirections.actionSplashFragmentToSignInFragment()
                Screen.TimetableScreen ->
                    SplashFragmentDirections.actionSplashFragmentToBottomNavGraph()
            }
        )
    }
}
