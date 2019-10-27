package com.kubsu.timetable.presentation.timetable

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val timetableUpdater: Updater<State, Action, Subscription, SideEffect> = { state, action ->
    when (action) {
        is Action.UpdateData ->
            UpdateResponse(
                state = state.copy(
                    currentSubscription = action.subscription,
                    progress = action.subscription != null
                ),
                sideEffects = if (action.subscription != null)
                    setOf(SideEffect.LoadCurrentTimetable(action.subscription))
                else
                    emptySet()
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