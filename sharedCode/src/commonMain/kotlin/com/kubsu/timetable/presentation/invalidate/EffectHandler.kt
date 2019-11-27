package com.kubsu.timetable.presentation.invalidate

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.fold
import com.kubsu.timetable.domain.interactor.appinfo.AppInfoInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InvalidateEffectHandler(
    private val appInfoInteractor: AppInfoInteractor
) : EffectHandler<Invidate.SideEffect, Invidate.Action> {
    override fun invoke(sideEffect: Invidate.SideEffect): Flow<Invidate.Action> = flow {
        when (sideEffect) {
            is Invidate.SideEffect.Invalidate ->
                emit(
                    appInfoInteractor
                        .invalidate()
                        .fold(
                            ifLeft = { Invidate.Action.Failure(it) },
                            ifRight = { Invidate.Action.Success }
                        )
                )
        }.checkWhenAllHandled()
    }
}