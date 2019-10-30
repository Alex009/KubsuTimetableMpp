package com.kubsu.timetable.presentation.registration

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val registrationUpdater: Updater<State, Action, Subscription, SideEffect> = { state, action ->
    when (action) {
        is Action.Registration ->
            if (action.password == action.repeatedPassword)
                UpdateResponse<State, Subscription, SideEffect>(
                    state = state.copy(progress = true),
                    sideEffects = setOf(
                        SideEffect.Registration(
                            email = action.email,
                            password = action.password
                        )
                    )
                )
            else
                UpdateResponse<State, Subscription, SideEffect>(
                    state,
                    subscription = Subscription.PasswordsVary
                )

        Action.ShowResult ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.Navigate(Screen.SignIn)
            )

        is Action.ShowRegistrationFailure ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.ShowRegistrationFailure(action.failureList)
            )

        is Action.ShowDataFailure ->
            UpdateResponse(
                state = state.copy(progress = false),
                subscription = Subscription.ShowDataFailure(action.failureList)
            )
    }
}