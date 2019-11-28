package com.kubsu.timetable.presentation.signin

import com.egroden.teaco.UpdateResponse
import com.egroden.teaco.Updater

val signInUpdater =
    object : Updater<SignInState, SignInAction, SignInSubscription, SignInSideEffect> {
        override fun invoke(
            state: SignInState,
            action: SignInAction
        ): UpdateResponse<SignInState, SignInSubscription, SignInSideEffect> =
            when (action) {
                is SignInAction.SignIn ->
                    UpdateResponse(
                        state = state.copy(progress = true),
                        sideEffects = setOf(
                            SignInSideEffect.Authenticate(
                                action.email,
                                action.password
                            )
                        )
                    )

                SignInAction.Registration ->
                    UpdateResponse(
                        state,
                        subscription = SignInSubscription.Navigate(SignInScreen.Registration)
                    )

                SignInAction.ShowResult ->
                    UpdateResponse(
                        state = state.copy(progress = false),
                        subscription = SignInSubscription.Navigate(SignInScreen.Timetable)
                    )

                is SignInAction.ShowSignInFailure ->
                    UpdateResponse(
                        state = state.copy(progress = false),
                        subscription = SignInSubscription.ShowSignInFailure(action.failureList)
                    )

                is SignInAction.ShowDataFailure ->
                    UpdateResponse(
                        state = state.copy(progress = false),
                        subscription = SignInSubscription.ShowDataFailure(action.failureList)
                    )
            }
}