package com.kubsu.timetable.domain.entity.diff

data class DataDiffEntity(
    val userId: Int,
    val basename: Basename,
    val updatedIds: List<Int>,
    val deletedIds: List<Int>
)