package com.kubsu.timetable.presentation.splash

import com.egroden.teaco.EffectHandler
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SplashEffectHandler(
    private val authInteractor: AuthInteractor
) : EffectHandler<SideEffect, Action> {
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            is SideEffect.Initiate ->
                emit(
                    if (authInteractor.isUserAuthenticated())
                        Action.ShowTimetableScreen
                    else
                        Action.ShowSignInScreen
                )
        }
    }
}