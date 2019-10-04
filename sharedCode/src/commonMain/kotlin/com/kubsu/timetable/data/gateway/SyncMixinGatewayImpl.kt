package com.kubsu.timetable.data.gateway

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.db.diff.*
import com.kubsu.timetable.data.db.timetable.ClassQueries
import com.kubsu.timetable.data.db.timetable.LecturerQueries
import com.kubsu.timetable.data.db.timetable.SubscriptionQueries
import com.kubsu.timetable.data.db.timetable.TimetableQueries
import com.kubsu.timetable.data.mapper.diff.BasenameMapper
import com.kubsu.timetable.data.mapper.diff.DataDiffMapper
import com.kubsu.timetable.data.mapper.timetable.data.ClassMapper
import com.kubsu.timetable.data.mapper.timetable.data.LecturerMapper
import com.kubsu.timetable.data.mapper.timetable.data.SubscriptionMapper
import com.kubsu.timetable.data.mapper.timetable.data.TimetableMapper
import com.kubsu.timetable.data.network.client.update.UpdateDataNetworkClient
import com.kubsu.timetable.data.network.dto.response.SyncResponse
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.entity.diff.Basename
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import com.kubsu.timetable.domain.interactor.sync.SyncMixinGateway
import com.kubsu.timetable.flatMap

class SyncMixinGatewayImpl(
    private val subscriptionQueries: SubscriptionQueries,
    private val timetableQueries: TimetableQueries,
    private val lecturerQueries: LecturerQueries,
    private val classQueries: ClassQueries,
    private val dataDiffQueries: DataDiffQueries,
    private val updatedEntityQueries: UpdatedEntityQueries,
    private val deletedEntityQueries: DeletedEntityQueries,
    private val networkClient: UpdateDataNetworkClient
) : SyncMixinGateway {
    override fun registerDataDiff(entity: DataDiffEntity) {
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
            .flatMap { basename ->
                val basenameStr = BasenameMapper.value(basename)
                val dataDiffIdList = dataDiffList
                    .filter { it.basename == basenameStr }
                    .map { it.id }

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

    override suspend fun delete(list: List<DataDiffEntity>) {
        for (diff in list) {
            deleteData(diff.basename, diff.deletedIds)

            val diffId = dataDiffQueries
                .select(BasenameMapper.value(diff.basename), diff.userId)
                .executeAsOne()
                .id
            deletedEntityQueries.deleteByDataDiffId(diffId)
            updatedEntityQueries.deleteByDataDiffId(diffId)
        }
    }

    private fun deleteData(basename: Basename, deletedIds: List<Int>) =
        when (basename) {
            Basename.Subscription -> deletedIds.forEach(subscriptionQueries::deleteById)
            Basename.Timetable -> deletedIds.forEach(timetableQueries::deleteById)
            Basename.Lecturer -> deletedIds.forEach(lecturerQueries::deleteById)
            Basename.Class -> deletedIds.forEach(classQueries::deleteById)
        }

    override suspend fun diff(timestamp: Timestamp): Either<NetworkFailure, Pair<Timestamp, List<Basename>>> =
        networkClient
            .diff(timestamp.value)
            .map {
                val newTimestamp = Timestamp(it.timestamp)
                val list = it.basenameList.map { basename -> BasenameMapper.toEntity(basename) }
                newTimestamp to list
            }

    override suspend fun updateData(
        basename: Basename,
        availableDiff: DataDiffEntity,
        user: UserEntity
    ): Either<NetworkFailure, Unit> {
        val existsIds = availableDiff.updatedIds + availableDiff.deletedIds
        return sync(basename, existsIds, user.timestamp.value)
            .flatMap { (updatedIds, deletedIds) ->
                deleteData(basename, deletedIds)
                meta(basename, user.id, updatedIds)
            }
    }

    private suspend fun sync(
        basename: Basename,
        existsIds: List<Int>,
        timestamp: Long
    ): Either<NetworkFailure, SyncResponse> =
        when (basename) {
            Basename.Subscription ->
                networkClient.syncSubscription(timestamp, existsIds)

            Basename.Timetable ->
                networkClient.syncTimetable(timestamp, existsIds)

            Basename.Lecturer ->
                networkClient.syncLecturer(timestamp, existsIds)

            Basename.Class ->
                networkClient.syncClass(timestamp, existsIds)
        }

    override suspend fun meta(
        basename: Basename,
        userId: Int,
        updatedIds: List<Int>
    ): Either<NetworkFailure, Unit> =
        when (basename) {
            Basename.Subscription ->
                networkClient
                    .metaSubscription(updatedIds)
                    .map { list ->
                        for (networkDto in list)
                            subscriptionQueries.update(
                                SubscriptionMapper.toDbDto(
                                    networkDto,
                                    userId
                                )
                            )
                    }

            Basename.Timetable ->
                networkClient
                    .metaTimetable(updatedIds)
                    .map { list ->
                        for (timetable in list)
                            timetableQueries.update(TimetableMapper.toDbDto(timetable))
                    }

            Basename.Lecturer ->
                networkClient
                    .metaLecturer(updatedIds)
                    .map { list ->
                        for (lecturer in list)
                            lecturerQueries.update(LecturerMapper.toDbDto(lecturer))
                    }

            Basename.Class ->
                networkClient
                    .metaClass(updatedIds)
                    .map { list ->
                        for (`class` in list)
                            classQueries.update(ClassMapper.toDbDto(`class`))
                    }
        }
}