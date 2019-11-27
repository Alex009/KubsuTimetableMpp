package com.kubsu.timetable.presentation.signin

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val signInUpdater =
    object : Updater<SignIn.State, SignIn.Action, SignIn.Subscription, SignIn.SideEffect> {
        override fun invoke(
            state: SignIn.State,
            action: SignIn.Action
        ): UpdateResponse<SignIn.State, SignIn.Subscription, SignIn.SideEffect> =
            when (action) {
                is SignIn.Action.SignIn ->
                    UpdateResponse(
                        state = state.copy(progress = true),
                        sideEffects = setOf(
                            SignIn.SideEffect.Authenticate(
                                action.email,
                                action.password
                            )
                        )
                    )

                SignIn.Action.Registration ->
                    UpdateResponse(
                        state,
                        subscription = SignIn.Subscription.Navigate(SignIn.Screen.Registration)
                    )

                SignIn.Action.ShowResult ->
                    UpdateResponse(
                        state = state.copy(progress = false),
                        subscription = SignIn.Subscription.Navigate(SignIn.Screen.Timetable)
                    )

                is SignIn.Action.ShowSignInFailure ->
                    UpdateResponse(
                        state = state.copy(progress = false),
                        subscription = SignIn.Subscription.ShowSignInFailure(action.failureList)
                    )

                is SignIn.Action.ShowDataFailure ->
                    UpdateResponse(
                        state = state.copy(progress = false),
                        subscription = SignIn.Subscription.ShowDataFailure(action.failureList)
                    )
            }
}