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
) : EffectHandler<SubList.SideEffect, SubList.Action> {
    override fun invoke(sideEffect: SubList.SideEffect): Flow<SubList.Action> = flow {
        when (sideEffect) {
            SubList.SideEffect.LoadSubscriptionList ->
                subscriptionInteractor
                    .getAllSubscriptionsFlow()
                    .fold(
                        ifLeft = { emit(SubList.Action.ShowDataFailure(listOf(it))) },
                        ifRight = { flow ->
                            flow.collect {
                                emit(
                                    SubList.Action.SubscriptionListUploaded(
                                        it.map(SubscriptionModelMapper::toModel)
                                    )
                                )
                            }
                        }
                    )

            is SubList.SideEffect.UpdateSubscription ->
                subscriptionInteractor
                    .update(SubscriptionModelMapper.toEntity(sideEffect.subscription))
                    .fold(
                        ifLeft = { requestFailure ->
                            requestFailure.handle(
                                ifDomain = { emit(SubList.Action.ShowSubscriptionFailure(it)) },
                                ifData = { emit(SubList.Action.ShowDataFailure(it)) }
                            )
                        },
                        ifRight = {}
                    )

            is SubList.SideEffect.DeleteSubscription ->
                subscriptionInteractor
                    .deleteById(sideEffect.subscription.id)
                    .fold(
                        ifLeft = { emit(SubList.Action.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            displayedSubscriptionStorage.deleteIfEqual(sideEffect.subscription)
                        }
                    )

            is SubList.SideEffect.DisplayedSubscription ->
                displayedSubscriptionStorage.set(sideEffect.subscription)
        }.checkWhenAllHandled()
    }
}