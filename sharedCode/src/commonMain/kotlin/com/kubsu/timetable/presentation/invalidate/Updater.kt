package com.kubsu.timetable.presentation.invalidate

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val invalidateUpdater =
    object : Updater<InvidateState, InvidateAction, InvidateSubscription, InvidateSideEffect> {
        override fun invoke(
            state: InvidateState,
            action: InvidateAction
        ): UpdateResponse<InvidateState, InvidateSubscription, InvidateSideEffect> =
            when (action) {
                InvidateAction.Invalidate ->
                    UpdateResponse(
                        state = state.copy(progress = true),
                        sideEffects = setOf(InvidateSideEffect.Invalidate)
                    )

                InvidateAction.Success ->
                    UpdateResponse(
                        state = state.copy(progress = false),
                        subscription = InvidateSubscription.Navigate(InvidateScreen.Timetable)
                    )

                is InvidateAction.Failure ->
                    UpdateResponse(
                        state = state.copy(progress = false),
                        subscription = InvidateSubscription.ShowFailure(action.failure)
                    )
            }
}