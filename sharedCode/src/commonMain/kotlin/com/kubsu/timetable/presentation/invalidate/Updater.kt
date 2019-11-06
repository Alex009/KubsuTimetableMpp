package com.kubsu.timetable.presentation.invalidate

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val invalidateUpdater: Updater<State, Action, Subscription, SideEffect> = { state, action ->
    when (action) {
        Action.Invalidate ->
            UpdateResponse(
                state = state.copy(progress = true),
                sideEffects = setOf(SideEffect.Invalidate)
            )

        Action.Success ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.Navigate(Screen.Timetable)
            )

        is Action.Failure ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.ShowFailure(action.failure)
            )
    }
}