package com.kubsu.timetable.presentation.subscription.list

import com.egroden.teaco.EffectHandler
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionInteractor
import com.kubsu.timetable.presentation.timetable.mapper.SubscriptionModelMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SubscriptionListEffectHandler(
    private val subscriptionInteractor: SubscriptionInteractor
) : EffectHandler<SideEffect, Action> {
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            SideEffect.LoadSubscriptionList ->
                subscriptionInteractor
                    .getAll()
                    .fold(
                        ifLeft = { emit(Action.ShowFailure(it)) },
                        ifRight = {
                            emit(
                                Action.SubscriptionListUploaded(
                                    it.map(SubscriptionModelMapper::toModel)
                                )
                            )
                        }
                    )
        }
    }
}