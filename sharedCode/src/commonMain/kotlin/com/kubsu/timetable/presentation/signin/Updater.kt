package com.kubsu.timetable.presentation.signin

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val signInUpdater: Updater<State, Action, Subscription, SideEffect> = { state, action ->
    when (action) {
        is Action.SignIn ->
            UpdateResponse(
                state = state.copy(progress = true),
                sideEffects = setOf(SideEffect.Authenticate(action.email, action.password))
            )

        Action.Registration ->
            UpdateResponse(
                state,
                subscription = Subscription.Navigate(Screen.Registration)
            )

        Action.ShowResult ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.Navigate(Screen.Timetable)
            )

        is Action.ShowSignInFailure ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.ShowSignInFailure(action.failureList)
            )

        is Action.ShowDataFailure ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.ShowDataFailure(action.failureList)
            )
    }
}