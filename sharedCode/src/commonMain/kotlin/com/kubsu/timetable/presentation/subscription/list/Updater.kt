package com.kubsu.timetable.presentation.subscription.list

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val subscriptionListUpdater =
    object : Updater<SubListState, SubListAction, SubListSubscription, SubListSideEffect> {
        override fun invoke(
            state: SubListState,
            action: SubListAction
        ): UpdateResponse<SubListState, SubListSubscription, SubListSideEffect> =
            when (action) {
                SubListAction.UpdateData ->
                    UpdateResponse(
                        state = state.copy(progress = true),
                        sideEffects = setOf(SubListSideEffect.LoadSubscriptionList)
                    )

                SubListAction.CreateSubscription ->
                    UpdateResponse(
                        state,
                        subscription = SubListSubscription.Navigate(SubListScreen.CreateSubscription)
                    )

                is SubListAction.SubscriptionListUploaded ->
                    UpdateResponse(
                        state = state.copy(
                            progress = false,
                            subscriptionList = action.subscriptionList
                        )
                    )

                is SubListAction.SubscriptionWasSelected ->
                    UpdateResponse(
                        state,
                        subscription = SubListSubscription.Navigate(SubListScreen.ShowTimetable),
                        sideEffects = setOf(SubListSideEffect.DisplayedSubscription(action.subscription))
                    )

                is SubListAction.DeleteSubscription ->
                    UpdateResponse(
                        state = state.copy(progress = true),
                        sideEffects = setOf(SubListSideEffect.DeleteSubscription(action.subscription))
                    )

                is SubListAction.ChangeSubscriptionStatus ->
                    UpdateResponse(
                        state = state.copy(progress = true),
                        sideEffects = setOf(
                            SubListSideEffect.UpdateSubscription(
                                action.subscription.copy(isMain = !action.subscription.isMain)
                            )
                        )
                    )

                is SubListAction.ShowSubscriptionFailure ->
                    UpdateResponse(
                        state = state.copy(progress = false),
                        subscription = SubListSubscription.ShowSubscriptionFailure(action.failureList)
                    )

                is SubListAction.ShowDataFailure ->
                    UpdateResponse(
                        state = state.copy(progress = false),
                        subscription = SubListSubscription.ShowDataFailure(action.failureList)
                    )
            }
}