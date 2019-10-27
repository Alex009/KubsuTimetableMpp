package com.kubsu.timetable.presentation.timetable

import com.egroden.teaco.EffectHandler
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.domain.entity.timetable.data.TypeOfWeek
import com.kubsu.timetable.domain.entity.timetable.data.UniversityInfoEntity
import com.kubsu.timetable.domain.interactor.timetable.TimetableInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import com.kubsu.timetable.presentation.timetable.mapper.SubscriptionModelMapper
import com.kubsu.timetable.presentation.timetable.mapper.TimetableModelMapper
import com.kubsu.timetable.presentation.timetable.mapper.UniversityInfoModelMapper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*

class TimetableEffectHandler(
    private val timetableInteractor: TimetableInteractor
) : EffectHandler<SideEffect, Action> {
    @UseExperimental(FlowPreview::class, InternalCoroutinesApi::class)
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            is SideEffect.LoadCurrentTimetable ->
                timetableInteractor
                    .getAllTimetables(
                        SubscriptionModelMapper.toEntity(sideEffect.subscription)
                    )
                    .flatMapMerge { toActionFlow(it) }
                    .collect(this)
        }.checkWhenAllHandled()
    }

    private fun toActionFlow(timetableList: List<TimetableEntity>): Flow<Action> =
        if (timetableList.isNotEmpty())
            timetableInteractor
                .getUniversityData(timetableList.first())
                .map {
                    createActionShowTimetable(it, timetableList)
                }
        else
            flowOf()

    private fun createActionShowTimetable(
        universityInfo: UniversityInfoEntity,
        timetableList: List<TimetableEntity>
    ) =
        Action.ShowTimetable(
            universityInfoModel = universityInfo
                .let(UniversityInfoModelMapper::toModel),
            numeratorTimetable = timetableList
                .firstOrNull { it.typeOfWeek == TypeOfWeek.Numerator }
                ?.let(TimetableModelMapper::toModel),
            denominatorTimetable = timetableList
                .firstOrNull { it.typeOfWeek == TypeOfWeek.Denominator }
                ?.let(TimetableModelMapper::toModel)
        )
}