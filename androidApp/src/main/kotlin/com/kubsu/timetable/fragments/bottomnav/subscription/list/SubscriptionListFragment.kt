package com.kubsu.timetable.fragments.bottomnav.subscription.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.egroden.teaco.*
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
import ru.whalemare.sheetmenu.ActionItem

class SubscriptionListFragment(
    featureFactory: (
        oldState: SubListState?
    ) -> Feature<SubListAction, SubListSideEffect, SubListState, SubListSubscription>
) : BaseFragment(R.layout.subscription_list_fragment),
    Render<SubListState, SubListSubscription> {
    private val connector by androidConnectors(featureFactory) { bindAction(SubListAction.UpdateData) }

    private val progressEffect = UiEffect(false)
    private val subscriptionListEffect = UiEffect(emptyList<SubscriptionModel>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connector.connect(this, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val subscriptionAdapter = SubscriptionAdapter(
            onClick = {
                connector bindAction SubListAction.SubscriptionWasSelected(it)
            },
            onLongClick = { subscription ->
                sheetMenu(
                    context = view.context,
                    menu = R.menu.subscription_management_menu,
                    showIcons = true,
                    onClick = { it.handleClick(subscription) }
                )
            },
            changeSubscriptionStatus = {
                connector bindAction SubListAction.ChangeSubscriptionStatus(it)
            }
        )
        with(view.subscription_recycler_view) {
            setHasFixedSize(true)
            adapter = subscriptionAdapter
            layoutManager = LinearLayoutManager(context)
        }

        view.add_floating_action_button.setOnClickListener {
            connector bindAction SubListAction.CreateSubscription
        }

        progressEffect bind view.progress_bar::setVisibleStatus
        subscriptionListEffect bind { subscriptionList ->
            val listIsEmpty = subscriptionList.isEmpty()
            view.empty_list_layout.isVisible = listIsEmpty && !progressEffect.value
            view.subscription_recycler_view.isVisible = !listIsEmpty

            subscriptionAdapter.setData(subscriptionList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressEffect.unbind()
        subscriptionListEffect.unbind()
    }

    override fun renderState(state: SubListState) {
        progressEffect.value = state.progress
        subscriptionListEffect.value = state.subscriptionList
    }

    override fun renderSubscription(subscription: SubListSubscription) =
        when (subscription) {
            is SubListSubscription.Navigate ->
                navigation(subscription.screen)
            is SubListSubscription.ShowSubscriptionFailure ->
                Unit // because we don't change the subscription data on this screen
            is SubListSubscription.ShowDataFailure ->
                subscription.failureList.forEach(::notifyUserOfFailure)
        }

    private fun navigation(screen: SubListScreen) =
        when (screen) {
            SubListScreen.CreateSubscription ->
                safeNavigate(
                    BottomNavFragmentDirections
                        .actionBottomNavFragmentToCreateSubscriptionFragment()
                )

            SubListScreen.ShowTimetable ->
                safeNavigate(
                    SubscriptionListFragmentDirections
                        .actionSubscriptionListFragmentToTimetableFragment()
                )
        }

    private fun ActionItem.handleClick(subscription: SubscriptionModel) {
        when (id) {
            R.id.item_menu_delete ->
                requireActivity().materialAlert(
                    message = getString(R.string.are_you_sure_you_want_to_unsubscribe),
                    onOkButtonClick = {
                        connector bindAction SubListAction.DeleteSubscription(subscription)
                    },
                    onNoButtonClick = {}
                )
            else ->
                Unit
        }
    }
}
