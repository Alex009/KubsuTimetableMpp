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
) : EffectHandler<CreateSub.SideEffect, CreateSub.Action> {
    override fun invoke(sideEffect: CreateSub.SideEffect): Flow<CreateSub.Action> = flow {
        when (sideEffect) {
            CreateSub.SideEffect.SelectFacultyList ->
                interactor
                    .selectFacultyList()
                    .fold(
                        ifLeft = { emit(CreateSub.Action.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            emit(
                                CreateSub.Action.FacultyListUploaded(
                                    it.map(FacultyModelMapper::toModel)
                                )
                            )
                        }
                    )

            is CreateSub.SideEffect.SelectOccupationList ->
                interactor
                    .selectOccupationList(
                        FacultyModelMapper.toEntity(sideEffect.faculty)
                    )
                    .fold(
                        ifLeft = { emit(CreateSub.Action.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            emit(
                                CreateSub.Action.OccupationListUploaded(
                                    it.map(OccupationModelMapper::toModel)
                                )
                            )
                        }
                    )

            is CreateSub.SideEffect.SelectGroupList ->
                interactor
                    .selectGroupList(
                        OccupationModelMapper.toEntity(sideEffect.occupation)
                    )
                    .fold(
                        ifLeft = { emit(CreateSub.Action.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            emit(
                                CreateSub.Action.GroupListUploaded(
                                    it.map(GroupModelMapper::toModel)
                                )
                            )
                        }
                    )

            is CreateSub.SideEffect.SelectSubgroupList ->
                interactor
                    .selectSubgroupList(
                        GroupModelMapper.toEntity(sideEffect.group)
                    )
                    .fold(
                        ifLeft = { emit(CreateSub.Action.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            emit(
                                CreateSub.Action.SubgroupListUploaded(
                                    it.map(SubgroupModelMapper::toModel)
                                )
                            )
                        }
                    )

            is CreateSub.SideEffect.CreateSubscription ->
                interactor
                    .createSubscriptionTransaction(
                        subgroupId = sideEffect.subgroup.id,
                        subscriptionName = sideEffect.subscriptionName,
                        isMain = sideEffect.isMain
                    )
                    .fold(
                        ifLeft = { requestFailure ->
                            requestFailure.handle(
                                ifDomain = { emit(CreateSub.Action.ShowSubscriptionFailure(it)) },
                                ifData = { emit(CreateSub.Action.ShowDataFailure(it)) }
                            )
                        },
                        ifRight = {
                            emit(
                                CreateSub.Action.SubscriptionWasCreated(
                                    SubscriptionModelMapper.toModel(it)
                                )
                            )
                        }
                    )

            is CreateSub.SideEffect.DisplayedSubscription ->
                displayedSubscriptionStorage.set(sideEffect.subscription)
        }.checkWhenAllHandled()
    }
}