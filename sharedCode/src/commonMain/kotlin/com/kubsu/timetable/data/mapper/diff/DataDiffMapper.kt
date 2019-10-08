package com.kubsu.timetable.data.mapper.diff

import com.kubsu.timetable.data.db.diff.DeletedEntityDb
import com.kubsu.timetable.data.db.diff.UpdatedEntityDb
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity

object DataDiffMapper {
    fun toEntity(
        userId: Int,
        basename: Basename,
        updated: List<UpdatedEntityDb>,
        deleted: List<DeletedEntityDb>
    ): DataDiffEntity =
        DataDiffEntity(
            userId = userId,
            basename = basename,
            updatedIds = updated.map { it.updatedId },
            deletedIds = deleted.map { it.deletedId }
        )
}