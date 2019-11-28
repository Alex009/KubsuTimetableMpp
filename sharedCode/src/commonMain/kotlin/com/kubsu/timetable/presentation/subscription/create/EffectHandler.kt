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
) : EffectHandler<SubCreateSideEffect, SubCreateAction> {
    override fun invoke(sideEffect: SubCreateSideEffect): Flow<SubCreateAction> = flow {
        when (sideEffect) {
            SubCreateSideEffect.SelectFacultyList ->
                interactor
                    .selectFacultyList()
                    .fold(
                        ifLeft = { emit(SubCreateAction.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            emit(
                                SubCreateAction.FacultyListUploaded(
                                    it.map(FacultyModelMapper::toModel)
                                )
                            )
                        }
                    )

            is SubCreateSideEffect.SelectOccupationList ->
                interactor
                    .selectOccupationList(
                        FacultyModelMapper.toEntity(sideEffect.faculty)
                    )
                    .fold(
                        ifLeft = { emit(SubCreateAction.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            emit(
                                SubCreateAction.OccupationListUploaded(
                                    it.map(OccupationModelMapper::toModel)
                                )
                            )
                        }
                    )

            is SubCreateSideEffect.SelectGroupList ->
                interactor
                    .selectGroupList(
                        OccupationModelMapper.toEntity(sideEffect.occupation)
                    )
                    .fold(
                        ifLeft = { emit(SubCreateAction.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            emit(
                                SubCreateAction.GroupListUploaded(
                                    it.map(GroupModelMapper::toModel)
                                )
                            )
                        }
                    )

            is SubCreateSideEffect.SelectSubgroupList ->
                interactor
                    .selectSubgroupList(
                        GroupModelMapper.toEntity(sideEffect.group)
                    )
                    .fold(
                        ifLeft = { emit(SubCreateAction.ShowDataFailure(listOf(it))) },
                        ifRight = {
                            emit(
                                SubCreateAction.SubgroupListUploaded(
                                    it.map(SubgroupModelMapper::toModel)
                                )
                            )
                        }
                    )

            is SubCreateSideEffect.CreateSubscription ->
                interactor
                    .createSubscriptionTransaction(
                        subgroupId = sideEffect.subgroup.id,
                        subscriptionName = sideEffect.subscriptionName,
                        isMain = sideEffect.isMain
                    )
                    .fold(
                        ifLeft = { requestFailure ->
                            requestFailure.handle(
                                ifDomain = { emit(SubCreateAction.ShowSubscriptionFailure(it)) },
                                ifData = { emit(SubCreateAction.ShowDataFailure(it)) }
                            )
                        },
                        ifRight = {
                            emit(
                                SubCreateAction.SubscriptionWasCreated(
                                    SubscriptionModelMapper.toModel(it)
                                )
                            )
                        }
                    )

            is SubCreateSideEffect.DisplayedSubscription ->
                displayedSubscriptionStorage.set(sideEffect.subscription)
        }.checkWhenAllHandled()
    }
}