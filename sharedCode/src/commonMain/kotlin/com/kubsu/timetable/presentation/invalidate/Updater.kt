package com.kubsu.timetable.presentation.invalidate

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val invalidateUpdater =
    object : Updater<Invidate.State, Invidate.Action, Invidate.Subscription, Invidate.SideEffect> {
        override fun invoke(
            state: Invidate.State,
            action: Invidate.Action
        ): UpdateResponse<Invidate.State, Invidate.Subscription, Invidate.SideEffect> =
            when (action) {
                Invidate.Action.Invalidate ->
                    UpdateResponse(
                        state = state.copy(progress = true),
                        sideEffects = setOf(Invidate.SideEffect.Invalidate)
                    )

                Invidate.Action.Success ->
                    UpdateResponse(
                        state = state.copy(progress = false),
                        subscription = Invidate.Subscription.Navigate(Invidate.Screen.Timetable)
                    )

                is Invidate.Action.Failure ->
                    UpdateResponse(
                        state = state.copy(progress = false),
                        subscription = Invidate.Subscription.ShowFailure(action.failure)
                    )
            }
}