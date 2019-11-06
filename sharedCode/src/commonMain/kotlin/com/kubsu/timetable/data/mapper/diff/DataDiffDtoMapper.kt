package com.kubsu.timetable.data.mapper.diff

import com.kubsu.timetable.data.db.diff.DataDiffDb
import com.kubsu.timetable.data.db.diff.DeletedEntityDb
import com.kubsu.timetable.data.db.diff.UpdatedEntityDb
import com.kubsu.timetable.data.network.dto.diff.DataDiffNetworkDto
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity

object DataDiffDtoMapper {
    inline fun toMergedEntity(
        userId: Int,
        basename: Basename,
        dataDiffList: List<DataDiffDb>,
        getUpdated: (DataDiffDb) -> List<UpdatedEntityDb>,
        getDeleted: (DataDiffDb) -> List<DeletedEntityDb>
    ): DataDiffEntity.Merged {
        val handledDataDiffList = dataDiffList.filter { it.handled }
        val rawDataDiffList = dataDiffList - handledDataDiffList
        return DataDiffEntity.Merged(
            userId = userId,
            basename = basename,
            raw = DataDiffEntity.Raw(
                userId = userId,
                basename = basename,
                updatedIds = rawDataDiffList.map(getUpdated).flatten().map { it.updatedId },
                deletedIds = rawDataDiffList.map(getDeleted).flatten().map { it.deletedId }
            ),
            handled = DataDiffEntity.Handled(
                userId = userId,
                basename = basename,
                updatedIds = handledDataDiffList.map(getUpdated).flatten().map { it.updatedId },
                deletedIds = handledDataDiffList.map(getDeleted).flatten().map { it.deletedId }
            )
        )
    }

    fun toEntity(networkDto: DataDiffNetworkDto): DataDiffEntity.Raw {
        val ids = networkDto.silentIds + networkDto.noisyIds
        return DataDiffEntity.Raw(
            userId = networkDto.userId,
            basename = BasenameDtoMapper.toEntity(networkDto.basename),
            updatedIds = if (networkDto.messageTitle == "updating") ids else emptyList(),
            deletedIds = if (networkDto.messageTitle == "deleting") ids else emptyList()
        )
    }
}