package com.kubsu.timetable.data.gateway

import com.egroden.teaco.Either
import com.egroden.teaco.flatMap
import com.egroden.teaco.map
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.db.diff.*
import com.kubsu.timetable.data.db.timetable.*
import com.kubsu.timetable.data.mapper.diff.BasenameDtoMapper
import com.kubsu.timetable.data.mapper.diff.DataDiffDtoMapper
import com.kubsu.timetable.data.mapper.timetable.data.*
import com.kubsu.timetable.data.network.client.update.UpdateDataNetworkClient
import com.kubsu.timetable.data.network.dto.timetable.data.*
import com.kubsu.timetable.data.storage.user.session.SessionStorage
import com.kubsu.timetable.data.storage.user.session.getEitherFailure
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import com.kubsu.timetable.domain.interactor.sync.SyncMixinGateway

class SyncMixinGatewayImpl(
    private val subscriptionQueries: SubscriptionQueries,
    private val timetableQueries: TimetableQueries,
    private val lecturerQueries: LecturerQueries,
    private val classQueries: ClassQueries,
    private val dataDiffQueries: DataDiffQueries,
    private val universityInfoQueries: UniversityInfoQueries,
    private val updatedEntityQueries: UpdatedEntityQueries,
    private val deletedEntityQueries: DeletedEntityQueries,
    private val networkClient: UpdateDataNetworkClient,
    private val sessionStorage: SessionStorage
) : SyncMixinGateway {
    override suspend fun registerDataDiff(entity: DataDiffEntity) {
        dataDiffQueries.update(
            basename = BasenameDtoMapper.value(entity.basename),
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
                val basenameStr = BasenameDtoMapper.value(basename)
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

                    DataDiffDtoMapper.toEntity(userId, basename, updated, deleted)
                }
            }
    }

    override suspend fun delete(list: List<DataDiffEntity>) {
        for (diff in list) {
            deleteData(diff.basename, diff.deletedIds)

            val diffId = dataDiffQueries
                .select(BasenameDtoMapper.value(diff.basename), diff.userId)
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
            Basename.UniversityInfo -> deletedIds.forEach(universityInfoQueries::deleteById)
        }

    override suspend fun diff(): Either<DataFailure, Pair<Timestamp, List<Basename>>> =
        sessionStorage
            .getEitherFailure()
            .flatMap { session ->
                networkClient
                    .diff(session)
                    .map {
                        val newTimestamp = Timestamp(it.timestamp)
                        val list = it.basenameList.map { basename ->
                            BasenameDtoMapper.toEntity(basename)
                        }
                        newTimestamp to list
                    }
            }

    override suspend fun updateData(
        basename: Basename,
        availableDiff: DataDiffEntity
    ): Either<DataFailure, Unit> {
        val existsIds = availableDiff.updatedIds + availableDiff.deletedIds
        return sessionStorage
            .getEitherFailure()
            .flatMap { session ->
                networkClient
                    .sync(
                        session = session,
                        basename = BasenameDtoMapper.value(basename),
                        existsIds = existsIds
                    ).flatMap { (updatedIds, deletedIds) ->
                        deleteData(basename, deletedIds)
                        meta(basename, updatedIds)
                    }
            }
    }

    override suspend fun meta(
        basename: Basename,
        updatedIds: List<Int>
    ): Either<DataFailure, Unit> =
        sessionStorage
            .getEitherFailure()
            .flatMap { session ->
                val strBasename = BasenameDtoMapper.value(basename)
                when (basename) {
                    Basename.Subscription ->
                        networkClient
                            .meta<SubscriptionNetworkDto>(session, strBasename, updatedIds)
                            .map { list ->
                                for (networkDto in list)
                                    subscriptionQueries.update(
                                        SubscriptionDtoMapper.toDbDto(networkDto)
                                    )
                            }

                    Basename.Timetable ->
                        networkClient
                            .meta<TimetableNetworkDto>(session, strBasename, updatedIds)
                            .map { list ->
                                for (timetable in list)
                                    timetableQueries.update(TimetableDtoMapper.toDbDto(timetable))
                            }

                    Basename.Lecturer ->
                        networkClient
                            .meta<LecturerNetworkDto>(session, strBasename, updatedIds)
                            .map { list ->
                                for (lecturer in list)
                                    lecturerQueries.update(LecturerDtoMapper.toDbDto(lecturer))
                            }

                    Basename.Class ->
                        networkClient
                            .meta<ClassNetworkDto>(session, strBasename, updatedIds)
                            .map { list ->
                                for (`class` in list)
                                    classQueries.update(ClassDtoMapper.toDbDto(`class`))
                            }

                    Basename.UniversityInfo ->
                        networkClient
                            .meta<UniversityInfoNetworkDto>(session, strBasename, updatedIds)
                            .map { list ->
                                for (info in list)
                                    universityInfoQueries.update(
                                        UniversityInfoDtoMapper.toDbDto(
                                            info
                                        )
                                    )
                            }
                }
            }
}