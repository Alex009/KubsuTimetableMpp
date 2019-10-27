package com.kubsu.timetable.presentation.signin

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.fold
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInEffectHandler(
    private val authInteractor: AuthInteractor
) : EffectHandler<SideEffect, Action> {
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            is SideEffect.Authenticate ->
                authInteractor
                    .signInTransaction(sideEffect.email, sideEffect.password)
                    .fold(
                        ifLeft = { requestFailure ->
                            requestFailure.handle(
                                ifDomain = { emit(Action.ShowSignInFailure(it)) },
                                ifData = { emit(Action.ShowDataFailure(it)) }
                            )
                        },
                        ifRight = { emit(Action.ShowResult) }
                    )
        }.checkWhenAllHandled()
    }
}