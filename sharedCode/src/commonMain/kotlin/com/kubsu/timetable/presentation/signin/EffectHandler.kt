package com.kubsu.timetable.presentation.signin

import com.egroden.teaco.EffectHandler
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInEffectHandler(
    private val authInteractor: AuthInteractor
) : EffectHandler<SideEffect, Action> {
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            is SideEffect.Authenticate ->
                authInteractor
                    .signIn(sideEffect.email, sideEffect.password)
                    .fold(
                        ifLeft = { emit(Action.ShowError(it)) },
                        ifRight = { emit(Action.ShowResult) }
                    )
        }
    }
}