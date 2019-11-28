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
) : EffectHandler<SubListSideEffect, SubListAction> {
    override fun invoke(sideEffect: SubListSideEffect): Flow<SubListAction> = flow {
        when (sideEffect) {
            SubListSideEffect.LoadSubscriptionList ->
                subscriptionInteractor
                    .getAllSubscriptionsFlow()
                    .fold(
                        ifLeft = { emit(SubListAction.ShowDataFailure(listOf(it))) },
                        ifRight = { flow ->
                            flow.collect {
                                emit(
                                    SubListAction.SubscriptionListUploaded(
                                        it.map(SubscriptionModelMapper::toModel)
                                    )
                                )
                            }
                        }
                    )

            is SubListSideEffect.UpdateSubscription ->
                subscriptionInteractor
                    .update(SubscriptionModelMapper.toEntity(sideEffect.subscription))
                    .fold(
                        ifLeft = { requestFailure ->
                            requestFailure.handle(
                                ifDomain = { emit(SubListAction.ShowSubscriptionFailure(it)) },
                                ifData = { emit(SubListAction.ShowDataFailure(it)) }
                            )
                        },
                        ifRight = {}
                    )

            is SubListSideEffect.DeleteSubscription ->
                subscriptionInteractor
                    .deleteById(sideEffect.subscription.id)
                    .fold(
                        ifLeft = { emit(SubListAction.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            displayedSubscriptionStorage.deleteIfEqual(sideEffect.subscription)
                        }
                    )

            is SubListSideEffect.DisplayedSubscription ->
                displayedSubscriptionStorage.set(sideEffect.subscription)
        }.checkWhenAllHandled()
    }
}