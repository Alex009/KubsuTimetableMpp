package com.kubsu.timetable.presentation.subscription.list

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val subscriptionListUpdater: Updater<State, Action, Subscription, SideEffect> = { state, action ->
    when (action) {
        Action.UpdateData ->
            UpdateResponse(
                state = state.copy(progress = true),
                sideEffects = setOf(SideEffect.LoadSubscriptionList)
            )

        Action.CreateSubscription ->
            UpdateResponse(
                state,
                subscription = Subscription.Navigate(Screen.CreateSubscription)
            )

        is Action.SubscriptionListUploaded ->
            UpdateResponse(
                state = state.copy(
                    progress = false,
                    subscriptionList = action.subscriptionList
                )
            )

        is Action.ShowFailure ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.ShowFailure(action.failure)
            )

        is Action.SubscriptionWasSelected ->
            UpdateResponse(
                state,
                subscription = Subscription.Navigate(
                    Screen.ShowTimetableForSubscription(action.subscription)
                )
            )
    }
}