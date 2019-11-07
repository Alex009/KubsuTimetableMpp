package com.kubsu.timetable.presentation.timetable

import com.egroden.teaco.EffectHandler
import com.kubsu.timetable.domain.entity.timetable.data.ClassEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.domain.entity.timetable.data.TypeOfWeek
import com.kubsu.timetable.domain.entity.timetable.data.UniversityInfoEntity
import com.kubsu.timetable.domain.interactor.timetable.TimetableInteractor
import com.kubsu.timetable.extensions.checkWhenAllHandled
import com.kubsu.timetable.extensions.withOldValue
import com.kubsu.timetable.presentation.timetable.mapper.ClassModelMapper
import com.kubsu.timetable.presentation.timetable.mapper.SubscriptionModelMapper
import com.kubsu.timetable.presentation.timetable.mapper.TimetableModelMapper
import com.kubsu.timetable.presentation.timetable.mapper.UniversityInfoModelMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*

class TimetableEffectHandler(
    private val timetableInteractor: TimetableInteractor
) : EffectHandler<SideEffect, Action> {
    @UseExperimental(
        InternalCoroutinesApi::class,
        ExperimentalCoroutinesApi::class
    )
    override fun invoke(sideEffect: SideEffect): Flow<Action> = flow {
        when (sideEffect) {
            is SideEffect.LoadCurrentTimetable ->
                timetableInteractor
                    .getAllTimetables(
                        SubscriptionModelMapper.toEntity(sideEffect.subscription)
                    )
                    .withOldValue(::saveDisplayedStatus)
                    .flatMapLatest(::toActionFlow)
                    .collect(this)

            is SideEffect.ChangesWasDisplayed ->
                timetableInteractor.changesWasDisplayed(
                    ClassModelMapper.toEntity(sideEffect.classModel)
                )
        }.checkWhenAllHandled()
    }

    private fun saveDisplayedStatus(
        oldList: List<TimetableEntity>,
        newList: List<TimetableEntity>
    ): List<TimetableEntity> {
        fun ClassEntity.findThisOnList(list: List<ClassEntity>): ClassEntity? =
            list.firstOrNull { item -> item.id == this.id }

        fun ClassEntity.copyWithDisplayStatus(other: ClassEntity): ClassEntity =
            copy(needToEmphasize = other.needToEmphasize)

        fun List<ClassEntity>.copyFromThisWithDisplayStatus(other: ClassEntity): ClassEntity =
            other.findThisOnList(this)
                ?.takeIf(ClassEntity::needToEmphasize)
                ?.let(other::copyWithDisplayStatus)
                ?: other

        fun TimetableEntity.copyWithSavingDisplayStatus(other: TimetableEntity) =
            copy(classList = classList.map(other.classList::copyFromThisWithDisplayStatus))

        fun TimetableEntity.findThisOnList(list: List<TimetableEntity>): TimetableEntity? =
            list.firstOrNull { item -> item.id == this.id }

        return newList.map { new ->
            new.findThisOnList(oldList)?.let(new::copyWithSavingDisplayStatus) ?: new
        }
    }

    private suspend fun toActionFlow(timetableList: List<TimetableEntity>): Flow<Action> =
        if (timetableList.isNotEmpty())
            timetableInteractor
                .getUniversityData(timetableList.first())
                .map {
                    createActionShowTimetable(it, timetableList)
                }
        else
            flowOf(
                Action.ShowTimetable(
                    universityInfoModel = null,
                    numeratorTimetable = null,
                    denominatorTimetable = null
                )
            )

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