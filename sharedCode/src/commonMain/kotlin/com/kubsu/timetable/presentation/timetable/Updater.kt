package com.kubsu.timetable.presentation.timetable

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val timetableUpdater: Updater<State, Action, Subscription, SideEffect> = { state, action ->
    when (action) {
        is Action.UpdateData ->
            UpdateResponse(
                state = state.copy(
                    currentSubscription = action.subscription,
                    progress = true
                ),
                sideEffects = setOf(SideEffect.LoadCurrentTimetable(action.subscription))
            )

        is Action.ShowFailure ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.ShowFailure(action.failure)
            )

        is Action.ShowTimetable ->
            UpdateResponse(
                state = state.copy(
                    progress = false,
                    universityInfoModel = action.universityInfoModel,
                    numeratorTimetable = action.numeratorTimetable,
                    denominatorTimetable = action.denominatorTimetable
                )
            )
    }
}