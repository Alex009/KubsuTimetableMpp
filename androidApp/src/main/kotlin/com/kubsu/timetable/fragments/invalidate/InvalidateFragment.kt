package com.kubsu.timetable.fragments.invalidate

import android.os.Bundle
import android.view.View
import com.egroden.teaco.*
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.invalidate.Invidate
import com.kubsu.timetable.utils.*
import kotlinx.android.synthetic.main.invalidate_fragment.view.*
import kotlinx.android.synthetic.main.progress_bar.view.*

class InvalidateFragment(
    featureFactory: (
        oldState: Invidate.State?
    ) -> Feature<Invidate.Action, Invidate.SideEffect, Invidate.State, Invidate.Subscription>
) : BaseFragment(R.layout.invalidate_fragment),
    Render<Invidate.State, Invidate.Subscription> {
    private val connector by androidConnectors(featureFactory)

    private val progressEffect = UiEffect(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(this, lifecycle)
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
                        connector bindAction Invidate.Action.Invalidate
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

    override fun renderState(state: Invidate.State) {
        progressEffect.value = state.progress
    }

    override fun renderSubscription(subscription: Invidate.Subscription) =
        when (subscription) {
            is Invidate.Subscription.Navigate ->
                navigation(subscription.screen)
            is Invidate.Subscription.ShowFailure ->
                notifyUserOfFailure(subscription.failure)
        }

    private fun navigation(screen: Invidate.Screen) =
        safeNavigate(
            when (screen) {
                Invidate.Screen.Timetable ->
                    InvalidateFragmentDirections.actionInvalidateFragmentToBottomNavFragment()
            }
        )
}
