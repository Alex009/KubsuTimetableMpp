package com.kubsu.timetable.presentation.signin

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val signInUpdater: Updater<State, Action, Subscription, SideEffect> = { state, action ->
    when (action) {
        is Action.SignIn ->
            UpdateResponse(
                state = state.copy(inProgress = true),
                sideEffects = setOf(SideEffect.Authenticate(action.email, action.password))
            )

        Action.Registration ->
            UpdateResponse(
                state,
                subscription = Subscription.Navigate(Screen.Registration)
            )

        Action.ShowResult ->
            UpdateResponse(
                state = state.copy(inProgress = false),
                subscription = Subscription.Navigate(Screen.Timetable)
            )

        is Action.ShowError ->
            UpdateResponse(
                state = state.copy(inProgress = false),
                subscription = Subscription.ShowFailure(action.failure)
            )
    }
}