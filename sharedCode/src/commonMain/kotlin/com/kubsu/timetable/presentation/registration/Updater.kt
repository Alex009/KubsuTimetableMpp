package com.kubsu.timetable.presentation.registration

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val registrationUpdater = object :
    Updater<RegistrationState, RegistrationAction, RegistrationSubscription, RegistrationSideEffect> {
    override fun invoke(
        state: RegistrationState,
        action: RegistrationAction
    ): UpdateResponse<RegistrationState, RegistrationSubscription, RegistrationSideEffect> =
        when (action) {
            is RegistrationAction.Registration ->
                if (action.password == action.repeatedPassword)
                    UpdateResponse<RegistrationState, RegistrationSubscription, RegistrationSideEffect>(
                        state = state.copy(progress = true),
                        sideEffects = setOf(
                            RegistrationSideEffect.Registration(
                                email = action.email,
                                password = action.password
                            )
                        )
                    )
                else
                    UpdateResponse<RegistrationState, RegistrationSubscription, RegistrationSideEffect>(
                        state,
                        subscription = RegistrationSubscription.PasswordsVary
                    )

            RegistrationAction.ShowResult ->
                UpdateResponse(
                    state = state.copy(progress = false),
                    subscription = RegistrationSubscription.Navigate(RegistrationScreen.CreateSubscription)
                )

            is RegistrationAction.ShowRegistrationFailure ->
                UpdateResponse(
                    state = state.copy(progress = false),
                    subscription = RegistrationSubscription.ShowRegistrationFailure(action.failureList)
                )

            is RegistrationAction.ShowDataFailure ->
                UpdateResponse(
                    state = state.copy(progress = false),
                    subscription = RegistrationSubscription.ShowDataFailure(action.failureList)
                )
        }
}