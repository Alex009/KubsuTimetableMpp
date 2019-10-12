package com.kubsu.timetable.presentation.registration

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val registrationUpdater: Updater<State, Action, Subscription, SideEffect> = { state, action ->
    when (action) {
        is Action.Registration ->
            UpdateResponse(
                state = state.copy(inProgress = true),
                sideEffects = setOf(SideEffect.Registration(action.email, action.password))
            )

        Action.ShowResult ->
            UpdateResponse(
                state = state.copy(inProgress = false),
                subscription = Subscription.Navigate(Screen.SignIn)
            )

        is Action.ShowError ->
            UpdateResponse(
                state = state.copy(inProgress = false),
                subscription = Subscription.ShowFailure(action.failure)
            )
    }
}