package com.kubsu.timetable.presentation.timetable.mapper

import com.kubsu.timetable.domain.entity.timetable.data.ClassEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.presentation.timetable.model.TimetableInfoToDisplay
import com.kubsu.timetable.presentation.timetable.model.TimetableModel
import com.soywiz.klock.DayOfWeek

object TimetableModelMapper {
    fun toEntity(model: TimetableModel): TimetableEntity =
        TimetableEntity(
            id = model.id,
            typeOfWeek = TypeOfWeekModelMapper.toEntity(model.typeOfWeek),
            facultyId = model.facultyId,
            subgroupId = model.subgroupId,
            classList = getClassList(model)
        )

    fun toModel(entity: TimetableEntity): TimetableModel =
        TimetableModel(
            id = entity.id,
            typeOfWeek = TypeOfWeekModelMapper.toModel(entity.typeOfWeek),
            facultyId = entity.facultyId,
            subgroupId = entity.subgroupId,
            infoList = getTimetableInfoList(entity)
        )

    private fun getClassList(model: TimetableModel): List<ClassEntity> =
        model.infoList
            .filterIsInstance<TimetableInfoToDisplay.Class>()
            .map { ClassModelMapper.toEntity(it.classModel) }

    private fun getTimetableInfoList(timetableEntity: TimetableEntity): List<TimetableInfoToDisplay> {
        val result = mutableListOf<TimetableInfoToDisplay>()
        val daysList = DayOfWeek.values().sortedBy { it.index1Monday }
        for (day in daysList) {
            val classList: List<ClassEntity> = timetableEntity
                .classList
                .filter { it.day == day }

            if (classList.isNotEmpty()) {
                result.add(
                    TimetableInfoToDisplay.Day(
                        index = DayModelMapper.value(day),
                        dayOfWeek = day
                    )
                )
                result.addAll(
                    classList
                        .map(ClassModelMapper::toModel)
                        .map(TimetableInfoToDisplay::Class)
                )
            }
        }

        return result
    }
}