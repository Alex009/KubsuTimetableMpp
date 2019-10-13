package com.kubsu.timetable.presentation.registration

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val registrationUpdater: Updater<State, Action, Subscription, SideEffect> = { state, action ->
    when (action) {
        is Action.Registration ->
            UpdateResponse(
                state = state.copy(progress = true),
                sideEffects = setOf(SideEffect.Registration(action.email, action.password))
            )

        Action.ShowResult ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.Navigate(Screen.SignIn)
            )

        is Action.ShowFailure ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.ShowFailure(action.failure)
            )
    }
}