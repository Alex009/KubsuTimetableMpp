package com.kubsu.timetable.presentation.signin

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.fold
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInEffectHandler(
    private val authInteractor: AuthInteractor
) : EffectHandler<SignIn.SideEffect, SignIn.Action> {
    override fun invoke(sideEffect: SignIn.SideEffect): Flow<SignIn.Action> = flow {
        when (sideEffect) {
            is SignIn.SideEffect.Authenticate ->
                authInteractor
                    .signInTransaction(sideEffect.email, sideEffect.password)
                    .fold(
                        ifLeft = { requestFailure ->
                            requestFailure.handle(
                                ifDomain = { emit(SignIn.Action.ShowSignInFailure(it)) },
                                ifData = { emit(SignIn.Action.ShowDataFailure(it)) }
                            )
                        },
                        ifRight = { emit(SignIn.Action.ShowResult) }
                    )
        }.checkWhenAllHandled()
    }
}