package com.kubsu.timetable.presentation.app

import com.egroden.teaco.EffectHandler
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import com.kubsu.timetable.domain.interactor.sync.SyncMixinInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class AppEffectHandler(
    private val authInteractor: AuthInteractor,
    private val syncMixinInteractor: SyncMixinInteractor
) : EffectHandler<SideEffect, Action> {
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            is SideEffect.Initiate -> {
                authInteractor
                    .updateToken()
                syncMixinInteractor
                    .updateData()
                    .collect()
            }
        }.checkWhenAllHandled()
    }
}