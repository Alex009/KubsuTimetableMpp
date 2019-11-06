package com.kubsu.timetable.domain.entity.diff

import com.kubsu.timetable.domain.entity.Basename

sealed class DataDiffEntity {
    class Handled(
        val userId: Int,
        val basename: Basename,
        val updatedIds: List<Int>,
        val deletedIds: List<Int>
    ) : DataDiffEntity()

    class Raw(
        val userId: Int,
        val basename: Basename,
        val updatedIds: List<Int>,
        val deletedIds: List<Int>
    ) : DataDiffEntity()

    class Merged(
        val userId: Int,
        val basename: Basename,
        val handled: Handled,
        val raw: Raw
    ) : DataDiffEntity() {
        val updatedIds: List<Int> = handled.updatedIds + raw.updatedIds
        val deletedIds: List<Int> = handled.deletedIds + raw.deletedIds
    }
}