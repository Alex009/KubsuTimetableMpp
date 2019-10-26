package com.kubsu.timetable.presentation.splash

import com.egroden.teaco.EffectHandler
import com.kubsu.timetable.domain.interactor.userInfo.UserInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SplashEffectHandler(
    private val userInteractor: UserInteractor
) : EffectHandler<SideEffect, Action> {
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            is SideEffect.Initiate -> {
                emit(
                    if (userInteractor.getCurrentUserOrNull() != null)
                        Action.ShowTimetableScreen
                    else
                        Action.ShowSignInScreen
                )
            }
        }.checkWhenAllHandled()
    }
}