package com.kubsu.timetable.presentation.timetable

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val timetableUpdater = object :
    Updater<TimetableState, TimetableAction, TimetableSubscription, TimetableSideEffect> {
    override fun invoke(
        state: TimetableState,
        action: TimetableAction
    ): UpdateResponse<TimetableState, TimetableSubscription, TimetableSideEffect> =
        when (action) {
            is TimetableAction.LoadData ->
                UpdateResponse(
                    state = state.copy(
                        currentSubscription = action.subscription,
                        progress = action.subscription != null
                    ),
                    sideEffects = if (action.subscription != null)
                        setOf(TimetableSideEffect.LoadCurrentTimetable(action.subscription))
                    else
                        emptySet()
                )

            is TimetableAction.ShowTimetable ->
                UpdateResponse(
                    state = state.copy(
                        progress = false,
                        universityInfoModel = action.universityInfoModel,
                        numeratorTimetable = action.numeratorTimetable,
                        denominatorTimetable = action.denominatorTimetable
                    )
                )

            is TimetableAction.WasDisplayed ->
                UpdateResponse(
                    state = state,
                    sideEffects = if (action.classModel.needToEmphasize)
                        setOf(TimetableSideEffect.ChangesWasDisplayed(action.classModel))
                    else
                        setOf()
                )
        }
}