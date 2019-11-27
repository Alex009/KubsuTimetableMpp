package com.kubsu.timetable.presentation.timetable

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val timetableUpdater = object :
    Updater<Timetable.State, Timetable.Action, Timetable.Subscription, Timetable.SideEffect> {
    override fun invoke(
        state: Timetable.State,
        action: Timetable.Action
    ): UpdateResponse<Timetable.State, Timetable.Subscription, Timetable.SideEffect> =
        when (action) {
            is Timetable.Action.LoadData ->
                UpdateResponse(
                    state = state.copy(
                        currentSubscription = action.subscription,
                        progress = action.subscription != null
                    ),
                    sideEffects = if (action.subscription != null)
                        setOf(Timetable.SideEffect.LoadCurrentTimetable(action.subscription))
                    else
                        emptySet()
                )

            is Timetable.Action.ShowTimetable ->
                UpdateResponse(
                    state = state.copy(
                        progress = false,
                        universityInfoModel = action.universityInfoModel,
                        numeratorTimetable = action.numeratorTimetable,
                        denominatorTimetable = action.denominatorTimetable
                    )
                )

            is Timetable.Action.WasDisplayed ->
                UpdateResponse(
                    state = state,
                    sideEffects = if (action.classModel.needToEmphasize)
                        setOf(Timetable.SideEffect.ChangesWasDisplayed(action.classModel))
                    else
                        setOf()
                )
        }
}