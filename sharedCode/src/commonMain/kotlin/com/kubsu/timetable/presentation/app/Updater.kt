package com.kubsu.timetable.presentation.app

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val appUpdater: Updater<State, Action, Subscription, SideEffect> = { state, action ->
    when (action) {
        Action.Start ->
            UpdateResponse(
                state,
                sideEffects = setOf(SideEffect.Initiate)
            )
    }
}