package com.kubsu.timetable.presentation.invalidate

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.fold
import com.kubsu.timetable.domain.interactor.appinfo.AppInfoInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InvalidateEffectHandler(
    private val appInfoInteractor: AppInfoInteractor
) : EffectHandler<SideEffect, Action> {
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            is SideEffect.Invalidate ->
                emit(
                    appInfoInteractor
                        .invalidate()
                        .fold(
                            ifLeft = { Action.Failure(it) },
                            ifRight = { Action.Success }
                        )
                )
        }.checkWhenAllHandled()
    }
}