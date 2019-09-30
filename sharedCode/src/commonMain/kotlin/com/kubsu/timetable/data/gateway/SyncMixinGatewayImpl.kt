package com.kubsu.timetable.data.gateway

import com.kubsu.timetable.data.db.diff.*
import com.kubsu.timetable.data.mapper.diff.BasenameMapper
import com.kubsu.timetable.data.mapper.diff.DataDiffMapper
import com.kubsu.timetable.data.network.NetworkClient
import com.kubsu.timetable.domain.entity.diff.Basename
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import com.kubsu.timetable.domain.interactor.sync.SyncMixinGateway

class SyncMixinGatewayImpl(
    private val dataDiffQueries: DataDiffQueries,
    private val updatedEntityQueries: UpdatedEntityQueries,
    private val deletedEntityQueries: DeletedEntityQueries,
    private val networkClient: NetworkClient
) : SyncMixinGateway {
    override suspend fun newDataDiff(entity: DataDiffEntity) {
        dataDiffQueries.update(
            basename = BasenameMapper.value(entity.basename),
            userId = entity.userId
        )
        val id = dataDiffQueries.lastInsertRowId().executeAsOne().toInt()

        for (updatedId in entity.updatedIds)
            updatedEntityQueries.update(id, updatedId)
        for (deletedId in entity.deletedIds)
            deletedEntityQueries.update(id, deletedId)
    }

    override suspend fun getAvailableDiffList(userId: Int): List<DataDiffEntity> {
        val dataDiffList = dataDiffQueries.selectByUserId(userId).executeAsList()

        return Basename
            .list
            .map { basename: Basename ->
                val basenameStr = BasenameMapper.value(basename)
                dataDiffList
                    .filter { it.basename == basenameStr }
                    .map { it.id } to basename
            }
            .flatMap { (dataDiffIdList, basename) ->
                dataDiffIdList.map { id ->
                    val updated: List<UpdatedEntityDb> = updatedEntityQueries
                        .selectByDataDiffId(id)
                        .executeAsList()

                    val deleted: List<DeletedEntityDb> = deletedEntityQueries
                        .selectByDataDiffId(id)
                        .executeAsList()

                    DataDiffMapper.toEntity(userId, basename, updated, deleted)
                }
            }
    }

    override suspend fun updateData(basename: Basename, updatedIds: List<Int>) {
        // TODO
    }
}