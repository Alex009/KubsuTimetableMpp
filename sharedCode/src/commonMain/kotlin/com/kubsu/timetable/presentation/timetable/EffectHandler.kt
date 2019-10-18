package com.kubsu.timetable.presentation.timetable

import com.egroden.teaco.EffectHandler
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.domain.entity.timetable.data.TypeOfWeek
import com.kubsu.timetable.domain.interactor.timetable.TimetableInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import com.kubsu.timetable.flatMap
import com.kubsu.timetable.presentation.timetable.mapper.SubscriptionModelMapper
import com.kubsu.timetable.presentation.timetable.mapper.TimetableModelMapper
import com.kubsu.timetable.presentation.timetable.mapper.UniversityInfoModelMapper
import com.kubsu.timetable.right
import kotlinx.coroutines.flow.Flow
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
                    .flatMap { timetableList ->
                        if (timetableList.isNotEmpty())
                            timetableInteractor
                                .getUniversityData(timetableList.first())
                                .map { timetableList to it }
                        else
                            (emptyList<TimetableEntity>() to null).right()
                    }
                    .fold(
                        ifLeft = { emit(Action.ShowFailure(it)) },
                        ifRight = { (timetableList, universityInfo) ->
                            emit(
                                Action.ShowTimetable(
                                    universityInfoModel = universityInfo
                                        ?.let(UniversityInfoModelMapper::toModel),
                                    numeratorTimetable = timetableList
                                        .firstOrNull { it.typeOfWeek == TypeOfWeek.Numerator }
                                        ?.let(TimetableModelMapper::toModel),
                                    denominatorTimetable = timetableList
                                        .firstOrNull { it.typeOfWeek == TypeOfWeek.Denominator }
                                        ?.let(TimetableModelMapper::toModel)
                                )
                            )
                        }
                    )
        }.checkWhenAllHandled()
    }
}