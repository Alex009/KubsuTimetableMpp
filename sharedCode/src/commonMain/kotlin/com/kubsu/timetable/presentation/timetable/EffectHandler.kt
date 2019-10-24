package com.kubsu.timetable.presentation.timetable

import com.egroden.teaco.EffectHandler
import com.egroden.teaco.fold
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.domain.entity.timetable.data.TypeOfWeek
import com.kubsu.timetable.domain.entity.timetable.data.UniversityInfoEntity
import com.kubsu.timetable.domain.interactor.timetable.TimetableInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import com.kubsu.timetable.presentation.timetable.mapper.SubscriptionModelMapper
import com.kubsu.timetable.presentation.timetable.mapper.TimetableModelMapper
import com.kubsu.timetable.presentation.timetable.mapper.UniversityInfoModelMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class TimetableEffectHandler(
    private val timetableInteractor: TimetableInteractor
) : EffectHandler<SideEffect, Action> {
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            is SideEffect.LoadCurrentTimetable ->
                timetableInteractor
                    .getAllTimetables(
                        SubscriptionModelMapper.toEntity(sideEffect.subscription)
                    )
                    .collect { either ->
                        either.fold(
                            ifLeft = { emit(Action.ShowFailure(it)) },
                            ifRight = { showTimetableList(it) }
                        )
                    }
        }.checkWhenAllHandled()
    }

    private suspend fun FlowCollector<Action>.showTimetableList(
        timetableList: List<TimetableEntity>
    ) {
        if (timetableList.isNotEmpty())
            timetableInteractor
                .getUniversityData(timetableList.first())
                .collect { either ->
                    emit(
                        either.fold(
                            ifLeft = Action::ShowFailure,
                            ifRight = { createActionShowTimetable(it, timetableList) }
                        )
                    )
                }
    }

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