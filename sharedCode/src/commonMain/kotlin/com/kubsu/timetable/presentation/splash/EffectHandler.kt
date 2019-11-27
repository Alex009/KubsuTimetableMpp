package com.kubsu.timetable.presentation.splash

import com.egroden.teaco.EffectHandler
import com.kubsu.timetable.domain.interactor.userInfo.UserInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SplashEffectHandler(
    private val userInteractor: UserInteractor
) : EffectHandler<Splash.SideEffect, Splash.Action> {
    override fun invoke(sideEffect: Splash.SideEffect): Flow<Splash.Action> = flow {
        when (sideEffect) {
            is Splash.SideEffect.Initiate -> {
                emit(
                    if (userInteractor.getCurrentUserOrNull() != null)
                        Splash.Action.ShowTimetableScreen
                    else
                        Splash.Action.ShowSignInScreen
                )
            }
        }.checkWhenAllHandled()
    }
}