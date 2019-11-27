package com.kubsu.timetable.presentation.registration

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val registrationUpdater = object :
    Updater<Registration.State, Registration.Action, Registration.Subscription, Registration.SideEffect> {
    override fun invoke(
        state: Registration.State,
        action: Registration.Action
    ): UpdateResponse<Registration.State, Registration.Subscription, Registration.SideEffect> =
        when (action) {
            is Registration.Action.Registration ->
                if (action.password == action.repeatedPassword)
                    UpdateResponse<Registration.State, Registration.Subscription, Registration.SideEffect>(
                        state = state.copy(progress = true),
                        sideEffects = setOf(
                            Registration.SideEffect.Registration(
                                email = action.email,
                                password = action.password
                            )
                        )
                    )
                else
                    UpdateResponse<Registration.State, Registration.Subscription, Registration.SideEffect>(
                        state,
                        subscription = Registration.Subscription.PasswordsVary
                    )

            Registration.Action.ShowResult ->
                UpdateResponse(
                    state = state.copy(progress = false),
                    subscription = Registration.Subscription.Navigate(Registration.Screen.CreateSubscription)
                )

            is Registration.Action.ShowRegistrationFailure ->
                UpdateResponse(
                    state = state.copy(progress = false),
                    subscription = Registration.Subscription.ShowRegistrationFailure(action.failureList)
                )

            is Registration.Action.ShowDataFailure ->
                UpdateResponse(
                    state = state.copy(progress = false),
                    subscription = Registration.Subscription.ShowDataFailure(action.failureList)
                )
        }
}