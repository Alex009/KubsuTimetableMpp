package com.kubsu.timetable.fragments.subscription.create

import android.os.Bundle
import com.egroden.teaco.*
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.subscription.create.*
import com.kubsu.timetable.utils.getNavControllerOrNull

class CreateSubscriptionFragment(
    teaFeature: TeaFeature<Action, SideEffect, State, Subscription>,
    stateParser: StateParser<State>
) : BaseFragment(R.layout.create_subscription_fragment) {
    private val connector by androidConnectors(teaFeature, stateParser) {
        bindAction(Action.LoadFacultyList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(::render, ::render, lifecycle)
    }

    private fun render(state: State) {
        view?.run {

        }
    }

    private fun render(subscription: Subscription) =
        when (subscription) {
            is Subscription.Navigate -> navigation(subscription.screen)
            is Subscription.ShowFailure -> showFailure(subscription.failure)
        }

    private fun navigation(screen: Screen) {
        getNavControllerOrNull()?.navigate(
            when (screen) {
                Screen.TimetableScreen -> {
                }
            }
        )
    }

    private fun showFailure(failure: DataFailure) {

    }
}
