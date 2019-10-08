package com.kubsu.timetable.domain.entity.diff

import com.kubsu.timetable.domain.entity.Basename

data class DataDiffEntity(
    val userId: Int,
    val basename: Basename,
    val updatedIds: List<Int>,
    val deletedIds: List<Int>
)