package com.kubsu.timetable.fragments.invalidate

import android.os.Bundle
import android.view.View
import com.egroden.teaco.Feature
import com.egroden.teaco.androidConnectors
import com.egroden.teaco.bindAction
import com.egroden.teaco.connect
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.invalidate.*
import com.kubsu.timetable.utils.*
import kotlinx.android.synthetic.main.invalidate_fragment.view.*
import kotlinx.android.synthetic.main.progress_bar.view.*

class InvalidateFragment(
    featureFactory: (oldState: State?) -> Feature<Action, SideEffect, State, Subscription>
) : BaseFragment(R.layout.invalidate_fragment) {
    private val connector by androidConnectors(featureFactory)

    private val progressEffect = UiEffect(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(::render, ::render, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view.toolbar) {
            setNavigationOnClickListener {
                popBackStack()
            }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_confirm -> {
                        connector bindAction Action.Invalidate
                        true
                    }

                    else -> false
                }
            }
        }

        progressEffect bind {
            view.progress_bar.setVisibleStatus(it)
            if (it)
                view.toolbar.navigationIcon = null
            else
                view.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressEffect.unbind()
    }

    private fun render(state: State) {
        progressEffect.value = state.progress
    }

    private fun render(subscription: Subscription) =
        when (subscription) {
            is Subscription.Navigate ->
                navigation(subscription.screen)
            is Subscription.ShowFailure ->
                notifyUserOfFailure(subscription.failure)
        }

    private fun navigation(screen: Screen) =
        safeNavigate(
            when (screen) {
                Screen.Timetable ->
                    InvalidateFragmentDirections.actionInvalidateFragmentToBottomNavFragment()
            }
        )
}
