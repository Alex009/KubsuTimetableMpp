package com.kubsu.timetable.presentation.subscription.mapper

import com.kubsu.timetable.domain.entity.timetable.select.SubgroupEntity
import com.kubsu.timetable.presentation.subscription.model.SubgroupModel

object SubgroupModelMapper {
    fun toModel(entity: SubgroupEntity): SubgroupModel =
        SubgroupModel(
            id = entity.id,
            number = entity.number,
            groupId = entity.groupId
        )

    fun toEntity(model: SubgroupModel): SubgroupEntity =
        SubgroupEntity(
            id = model.id,
            number = model.number,
            groupId = model.groupId
        )
}