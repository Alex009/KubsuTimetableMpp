package com.kubsu.timetable.presentation.subscription.create

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.fold
import com.kubsu.timetable.data.storage.displayed.subscription.DisplayedSubscriptionStorage
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import com.kubsu.timetable.presentation.subscription.mapper.FacultyModelMapper
import com.kubsu.timetable.presentation.subscription.mapper.GroupModelMapper
import com.kubsu.timetable.presentation.subscription.mapper.OccupationModelMapper
import com.kubsu.timetable.presentation.subscription.mapper.SubgroupModelMapper
import com.kubsu.timetable.presentation.timetable.mapper.SubscriptionModelMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CreateSubscriptionEffectHandler(
    private val interactor: SubscriptionInteractor,
    private val displayedSubscriptionStorage: DisplayedSubscriptionStorage
) : EffectHandler<SideEffect, Action> {
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            SideEffect.SelectFacultyList ->
                interactor
                    .selectFacultyList()
                    .fold(
                        ifLeft = { emit(Action.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            emit(
                                Action.FacultyListUploaded(
                                    it.map(FacultyModelMapper::toModel)
                                )
                            )
                        }
                    )

            is SideEffect.SelectOccupationList ->
                interactor
                    .selectOccupationList(
                        FacultyModelMapper.toEntity(sideEffect.faculty)
                    )
                    .fold(
                        ifLeft = { emit(Action.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            emit(
                                Action.OccupationListUploaded(
                                    it.map(OccupationModelMapper::toModel)
                                )
                            )
                        }
                    )

            is SideEffect.SelectGroupList ->
                interactor
                    .selectGroupList(
                        OccupationModelMapper.toEntity(sideEffect.occupation)
                    )
                    .fold(
                        ifLeft = { emit(Action.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            emit(
                                Action.GroupListUploaded(
                                    it.map(GroupModelMapper::toModel)
                                )
                            )
                        }
                    )

            is SideEffect.SelectSubgroupList ->
                interactor
                    .selectSubgroupList(
                        GroupModelMapper.toEntity(sideEffect.group)
                    )
                    .fold(
                        ifLeft = { emit(Action.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            emit(
                                Action.SubgroupListUploaded(
                                    it.map(SubgroupModelMapper::toModel)
                                )
                            )
                        }
                    )

            is SideEffect.CreateSubscription ->
                interactor
                    .create(
                        subgroupId = sideEffect.subgroup.id,
                        subscriptionName = sideEffect.subscriptionName,
                        isMain = sideEffect.isMain
                    )
                    .fold(
                        ifLeft = { requestFailure ->
                            requestFailure.handle(
                                ifDomain = { emit(Action.ShowSubscriptionFailure(it)) },
                                ifData = { emit(Action.ShowDataFailure(it)) }
                            )
                        },
                        ifRight = {
                            emit(
                                Action.SubscriptionWasCreated(
                                    SubscriptionModelMapper.toModel(it)
                                )
                            )
                        }
                    )

            is SideEffect.DisplayedSubscription ->
                displayedSubscriptionStorage.set(sideEffect.subscription)
        }.checkWhenAllHandled()
    }
}