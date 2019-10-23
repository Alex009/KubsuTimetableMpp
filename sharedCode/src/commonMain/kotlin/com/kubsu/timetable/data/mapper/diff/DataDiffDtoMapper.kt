package com.kubsu.timetable.data.mapper.diff

import com.kubsu.timetable.data.db.diff.DeletedEntityDb
import com.kubsu.timetable.data.db.diff.UpdatedEntityDb
import com.kubsu.timetable.data.network.dto.diff.DataDiffNetworkDto
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity

object DataDiffDtoMapper {
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

    fun toEntity(networkDto: DataDiffNetworkDto): DataDiffEntity =
        DataDiffEntity(
            userId = networkDto.userId,
            basename = BasenameDtoMapper.toEntity(networkDto.basename),
            updatedIds = if (networkDto.messageTitle == "updating") networkDto.ids else emptyList(),
            deletedIds = if (networkDto.messageTitle == "deleting") networkDto.ids else emptyList()
        )
}