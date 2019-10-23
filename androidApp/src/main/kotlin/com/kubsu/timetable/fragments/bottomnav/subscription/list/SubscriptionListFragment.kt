package com.kubsu.timetable.fragments.bottomnav.subscription.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.egroden.teaco.TeaFeature
import com.egroden.teaco.androidConnectors
import com.egroden.teaco.bindAction
import com.egroden.teaco.connect
import com.kubsu.timetable.R
import com.kubsu.timetable.base.BaseFragment
import com.kubsu.timetable.fragments.bottomnav.BottomNavFragmentDirections
import com.kubsu.timetable.fragments.bottomnav.subscription.list.adapter.SubscriptionAdapter
import com.kubsu.timetable.presentation.subscription.list.*
import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel
import com.kubsu.timetable.utils.*
import com.kubsu.timetable.utils.ui.materialAlert
import com.kubsu.timetable.utils.ui.sheetMenu
import kotlinx.android.synthetic.main.add_floating_action_button.view.*
import kotlinx.android.synthetic.main.progress_bar.view.*
import kotlinx.android.synthetic.main.subscription_list_fragment.view.*

class SubscriptionListFragment(
    teaFeature: TeaFeature<Action, SideEffect, State, Subscription>
) : BaseFragment(R.layout.subscription_list_fragment) {
    private val connector by androidConnectors(teaFeature) { bindAction(Action.UpdateData) }

    private val progressEffect = UiEffect(Visibility.INVISIBLE)
    private val subscriptionListEffect = UiEffect(emptyList<SubscriptionModel>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(::render, ::render, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val subscriptionAdapter = SubscriptionAdapter(
            onClick = {
                connector bindAction Action.SubscriptionWasSelected(it)
            },
            onLongClick = { subscription ->
                sheetMenu(
                    context = view.context,
                    menu = R.menu.subscription_management_menu,
                    showIcons = true,
                    onClick = { action ->
                        when (action.id) {
                            R.id.item_menu_delete ->
                                requireActivity().materialAlert(
                                    message = getString(R.string.are_you_sure_you_want_to_unsubscribe),
                                    onOkButtonClick = {
                                        connector bindAction Action.DeleteSubscription(subscription)
                                    },
                                    onNoButtonClick = {}
                                )
                            else ->
                                Unit
                        }
                    }
                )
            },
            changeSubscriptionStatus = {
                connector bindAction Action.ChangeSubscriptionStatus(it)
            }
        )
        with(view.subscription_recycler_view) {
            setHasFixedSize(true)
            adapter = subscriptionAdapter
            layoutManager = LinearLayoutManager(context)
        }

        view.add_floating_action_button.setOnClickListener {
            connector bindAction Action.CreateSubscription
        }

        progressEffect bind { view.progress_bar.visibility(it) }
        subscriptionListEffect bind { subscriptionList ->
            val listIsNotEmpty = subscriptionList.isNotEmpty()
            view.empty_list_layout.isVisible = !listIsNotEmpty
            view.subscription_recycler_view.isVisible = listIsNotEmpty

            subscriptionAdapter.setData(subscriptionList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressEffect.unbind()
        subscriptionListEffect.unbind()
    }

    private fun render(state: State) {
        progressEffect.value = if (state.progress) Visibility.VISIBLE else Visibility.INVISIBLE
        subscriptionListEffect.value = state.subscriptionList
    }

    private fun render(subscription: Subscription) =
        when (subscription) {
            is Subscription.Navigate ->
                navigation(subscription.screen)
            is Subscription.ShowSubscriptionFailure ->
                Unit // because we don't change the subscription data on this screen
            is Subscription.ShowDataFailure ->
                subscription.failureList.forEach(::notifyUserOfFailure)
        }

    private fun navigation(screen: Screen) =
        when (screen) {
            Screen.CreateSubscription ->
                safeNavigate(
                    BottomNavFragmentDirections
                        .actionBottomNavFragmentToCreateSubscriptionFragment()
                )

            Screen.ShowTimetable ->
                safeNavigate(
                    SubscriptionListFragmentDirections
                        .actionSubscriptionListFragmentToTimetableFragment()
                )
        }
}
