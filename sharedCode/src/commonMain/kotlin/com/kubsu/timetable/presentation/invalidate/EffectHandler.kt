package com.kubsu.timetable.presentation.invalidate

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.fold
import com.kubsu.timetable.domain.interactor.appinfo.AppInfoInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InvalidateEffectHandler(
    private val appInfoInteractor: AppInfoInteractor
) : EffectHandler<InvidateSideEffect, InvidateAction> {
    override fun invoke(sideEffect: InvidateSideEffect): Flow<InvidateAction> = flow {
        when (sideEffect) {
            is InvidateSideEffect.Invalidate ->
                emit(
                    appInfoInteractor
                        .invalidate()
                        .fold(
                            ifLeft = { InvidateAction.Failure(it) },
                            ifRight = { InvidateAction.Success }
                        )
                )
        }.checkWhenAllHandled()
    }
}