package com.kubsu.timetable.presentation.registration

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.fold
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegistrationEffectHandler(
    private val authInteractor: AuthInteractor
) : EffectHandler<SideEffect, Action> {
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            is SideEffect.Registration ->
                authInteractor
                    .registrationUser(
                        email = sideEffect.email,
                        password = sideEffect.password
                    )
                    .fold(
                        ifLeft = { requestFailure ->
                            requestFailure.handle(
                                ifDomain = { emit(Action.ShowRegistrationFailure(it)) },
                                ifData = { emit(Action.ShowDataFailure(it)) }
                            )
                        },
                        ifRight = { emit(Action.ShowResult) }
                    )
        }.checkWhenAllHandled()
    }
}