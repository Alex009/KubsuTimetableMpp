package com.kubsu.timetable.presentation.subscription.list

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater
import com.kubsu.timetable.extensions.update
import com.kubsu.timetable.presentation.timetable.model.SubscriptionModel

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

        is Action.SubscriptionWasSelected ->
            UpdateResponse(
                state,
                subscription = Subscription.Navigate(Screen.ShowTimetable),
                sideEffects = setOf(SideEffect.DisplayedSubscription(action.subscription))
            )

        is Action.ChangeSubscriptionStatus ->
            UpdateResponse(
                state = state.copy(progress = true),
                sideEffects = setOf(
                    SideEffect.UpdateSubscription(
                        action.subscription.copy(isMain = !action.subscription.isMain)
                    )
                )
            )

        is Action.SubscriptionWasUpdated ->
            UpdateResponse(
                state = state.copy(
                    progress = false,
                    subscriptionList = state
                        .subscriptionList
                        .update(action.subscription, SubscriptionModel::id)
                )
            )

        is Action.ShowSubscriptionFailure ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.ShowSubscriptionFailure(action.failureList)
            )

        is Action.ShowDataFailure ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.ShowDataFailure(action.failureList)
            )
    }
}