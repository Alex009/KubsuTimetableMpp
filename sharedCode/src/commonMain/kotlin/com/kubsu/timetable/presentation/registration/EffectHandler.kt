package com.kubsu.timetable.presentation.registration

import com.egroden.teaco.EffectHandler
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegistrationEffectHandler(
    private val authInteractor: AuthInteractor
) : EffectHandler<SideEffect, Action> {
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            is SideEffect.Registration ->
                authInteractor
                    .registrationUser(sideEffect.email, sideEffect.password)
                    .fold(
                        ifLeft = { emit(Action.ShowFailure(it)) },
                        ifRight = { emit(Action.ShowResult) }
                    )
        }
    }
}