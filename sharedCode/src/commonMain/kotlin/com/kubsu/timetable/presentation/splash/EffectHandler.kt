package com.kubsu.timetable.presentation.splash

import com.egroden.teaco.EffectHandler
import com.kubsu.timetable.domain.interactor.userInfo.UserInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SplashEffectHandler(
    private val userInteractor: UserInteractor
) : EffectHandler<SplashSideEffect, SplashAction> {
    override fun invoke(sideEffect: SplashSideEffect): Flow<SplashAction> = flow {
        when (sideEffect) {
            is SplashSideEffect.Initiate -> {
                emit(
                    if (userInteractor.getCurrentUserOrNull() != null)
                        SplashAction.ShowTimetableScreen
                    else
                        SplashAction.ShowSignInScreen
                )
            }
        }.checkWhenAllHandled()
    }
}