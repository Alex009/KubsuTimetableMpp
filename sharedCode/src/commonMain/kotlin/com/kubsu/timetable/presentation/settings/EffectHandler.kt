package com.kubsu.timetable.presentation.settings

import com.egroden.teaco.EffectHandler
import com.kubsu.timetable.data.storage.displayed.subscription.DisplayedSubscriptionStorage
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SettingsEffectHandler(
    private val authInteractor: AuthInteractor,
    private val displayedSubscriptionStorage: DisplayedSubscriptionStorage
) : EffectHandler<SideEffect, Action> {
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            SideEffect.Logout -> {
                displayedSubscriptionStorage.set(null)
                authInteractor.logout()
                emit(Action.SuccessLogout)
            }
        }.checkWhenAllHandled()
    }
}