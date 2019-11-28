package com.kubsu.timetable.fragments.invalidate

import android.os.Bundle
import android.view.View
import com.egroden.teaco.*
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.presentation.invalidate.*
import com.kubsu.timetable.utils.*
import kotlinx.android.synthetic.main.invalidate_fragment.view.*
import kotlinx.android.synthetic.main.progress_bar.view.*

class InvalidateFragment(
    featureFactory: (
        oldState: InvidateState?
    ) -> Feature<InvidateAction, InvidateSideEffect, InvidateState, InvidateSubscription>
) : BaseFragment(R.layout.invalidate_fragment),
    Render<InvidateState, InvidateSubscription> {
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
                        connector bindAction InvidateAction.Invalidate
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

    override fun renderState(state: InvidateState) {
        progressEffect.value = state.progress
    }

    override fun renderSubscription(subscription: InvidateSubscription) =
        when (subscription) {
            is InvidateSubscription.Navigate ->
                navigation(subscription.screen)
            is InvidateSubscription.ShowFailure ->
                notifyUserOfFailure(subscription.failure)
        }

    private fun navigation(screen: InvidateScreen) =
        safeNavigate(
            when (screen) {
                InvidateScreen.Timetable ->
                    InvalidateFragmentDirections.actionInvalidateFragmentToBottomNavFragment()
            }
        )
}
