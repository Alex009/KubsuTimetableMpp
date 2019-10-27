package com.kubsu.timetable.presentation.subscription.list

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.fold
import com.kubsu.timetable.data.storage.displayed.subscription.DisplayedSubscriptionStorage
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import com.kubsu.timetable.presentation.timetable.mapper.SubscriptionModelMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class SubscriptionListEffectHandler(
    private val subscriptionInteractor: SubscriptionInteractor,
    private val displayedSubscriptionStorage: DisplayedSubscriptionStorage
) : EffectHandler<SideEffect, Action> {
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            SideEffect.LoadSubscriptionList ->
                subscriptionInteractor
                    .getAllSubscriptionsFlow()
                    .fold(
                        ifLeft = { emit(Action.ShowDataFailure(listOf(it))) },
                        ifRight = { flow ->
                            flow.collect {
                                emit(
                                    Action.SubscriptionListUploaded(
                                        it.map(SubscriptionModelMapper::toModel)
                                    )
                                )
                            }
                        }
                    )

            is SideEffect.UpdateSubscription ->
                subscriptionInteractor
                    .update(SubscriptionModelMapper.toEntity(sideEffect.subscription))
                    .fold(
                        ifLeft = { requestFailure ->
                            requestFailure.handle(
                                ifDomain = { emit(Action.ShowSubscriptionFailure(it)) },
                                ifData = { emit(Action.ShowDataFailure(it)) }
                            )
                        },
                        ifRight = {}
                    )

            is SideEffect.DeleteSubscription ->
                subscriptionInteractor
                    .deleteById(sideEffect.subscription.id)
                    .fold(
                        ifLeft = { emit(Action.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            displayedSubscriptionStorage.deleteIfEqual(sideEffect.subscription)
                        }
                    )

            is SideEffect.DisplayedSubscription ->
                displayedSubscriptionStorage.set(sideEffect.subscription)
        }.checkWhenAllHandled()
    }
}