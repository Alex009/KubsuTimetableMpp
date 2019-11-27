package com.kubsu.timetable.presentation.subscription.list

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val subscriptionListUpdater =
    object : Updater<SubList.State, SubList.Action, SubList.Subscription, SubList.SideEffect> {
        override fun invoke(
            state: SubList.State,
            action: SubList.Action
        ): UpdateResponse<SubList.State, SubList.Subscription, SubList.SideEffect> =
            when (action) {
                SubList.Action.UpdateData ->
                    UpdateResponse(
                        state = state.copy(progress = true),
                        sideEffects = setOf(SubList.SideEffect.LoadSubscriptionList)
                    )

                SubList.Action.CreateSubscription ->
                    UpdateResponse(
                        state,
                        subscription = SubList.Subscription.Navigate(SubList.Screen.CreateSubscription)
                    )

                is SubList.Action.SubscriptionListUploaded ->
                    UpdateResponse(
                        state = state.copy(
                            progress = false,
                            subscriptionList = action.subscriptionList
                        )
                    )

                is SubList.Action.SubscriptionWasSelected ->
                    UpdateResponse(
                        state,
                        subscription = SubList.Subscription.Navigate(SubList.Screen.ShowTimetable),
                        sideEffects = setOf(SubList.SideEffect.DisplayedSubscription(action.subscription))
                    )

                is SubList.Action.DeleteSubscription ->
                    UpdateResponse(
                        state = state.copy(progress = true),
                        sideEffects = setOf(SubList.SideEffect.DeleteSubscription(action.subscription))
                    )

                is SubList.Action.ChangeSubscriptionStatus ->
                    UpdateResponse(
                        state = state.copy(progress = true),
                        sideEffects = setOf(
                            SubList.SideEffect.UpdateSubscription(
                                action.subscription.copy(isMain = !action.subscription.isMain)
                            )
                        )
                    )

                is SubList.Action.ShowSubscriptionFailure ->
                    UpdateResponse(
                        state = state.copy(progress = false),
                        subscription = SubList.Subscription.ShowSubscriptionFailure(action.failureList)
                    )

                is SubList.Action.ShowDataFailure ->
                    UpdateResponse(
                        state = state.copy(progress = false),
                        subscription = SubList.Subscription.ShowDataFailure(action.failureList)
                    )
            }
}