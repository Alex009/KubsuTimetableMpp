package com.kubsu.timetable.presentation.splash

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.fold
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SplashEffectHandler(
    private val authInteractor: AuthInteractor
) : EffectHandler<SideEffect, Action> {
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            is SideEffect.Initiate -> {
                authInteractor
                    .init()
                    .fold(
                        ifLeft = { emit(Action.ShowFailure(it)) },
                        ifRight = {
                            emit(
                                if (authInteractor.isUserAuthenticated())
                                    Action.ShowTimetableScreen
                                else
                                    Action.ShowSignInScreen
                            )
                        }
                    )
            }
        }.checkWhenAllHandled()
    }
}