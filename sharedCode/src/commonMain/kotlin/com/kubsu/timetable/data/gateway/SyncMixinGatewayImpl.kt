package com.kubsu.timetable.data.gateway

import com.egroden.teaco.Either
import com.egroden.teaco.flatMap
import com.egroden.teaco.map
import com.egroden.teaco.right
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.db.diff.*
import com.kubsu.timetable.data.db.timetable.*
import com.kubsu.timetable.data.mapper.diff.BasenameDtoMapper
import com.kubsu.timetable.data.mapper.diff.DataDiffDtoMapper
import com.kubsu.timetable.data.mapper.timetable.data.*
import com.kubsu.timetable.data.network.client.update.UpdateDataNetworkClient
import com.kubsu.timetable.data.network.dto.diff.FantasticFour
import com.kubsu.timetable.data.network.dto.timetable.data.*
import com.kubsu.timetable.data.storage.user.session.Session
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import com.kubsu.timetable.domain.interactor.sync.SyncMixinGateway
import com.kubsu.timetable.domain.interactor.timetable.AppInfoGateway
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway
import com.kubsu.timetable.extensions.filterPrevious
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.KSerializer

class SyncMixinGatewayImpl(
    private val subscriptionQueries: SubscriptionQueries,
    private val timetableQueries: TimetableQueries,
    private val lecturerQueries: LecturerQueries,
    private val classQueries: ClassQueries,
    private val dataDiffQueries: DataDiffQueries,
    private val universityInfoQueries: UniversityInfoQueries,
    private val updatedEntityQueries: UpdatedEntityQueries,
    private val deletedEntityQueries: DeletedEntityQueries,
    private val updateDataNetworkClient: UpdateDataNetworkClient,
    private val appInfoGateway: AppInfoGateway,
    private val userInfoGateway: UserInfoGateway
) : SyncMixinGateway {
    override suspend fun registerDataDiff(entity: DataDiffEntity) {
        val id = createAndGetId(
            basename = BasenameDtoMapper.value(entity.basename),
            userId = entity.userId
        )
        for (updatedId in entity.updatedIds)
            updatedEntityQueries.insert(id, updatedId)
        for (deletedId in entity.deletedIds)
            deletedEntityQueries.insert(id, deletedId)
    }

    private fun createAndGetId(basename: String, userId: Int): Int {
        dataDiffQueries.insert(basename, userId)
        return dataDiffQueries
            .selectIds(basename, userId)
            .executeAsList()
            .last()
    }

    override fun getAvailableDiffList(): List<DataDiffEntity> =
        userInfoGateway
            .getCurrentUserOrNull()
            ?.id
            ?.let {
                dataDiffQueries
                    .selectAll()
                    .executeAsList()
                    .mapToEntityForUser(it)
            }
            ?: emptyList()

    override fun dataDiffListFlow(): Flow<List<DataDiffEntity>> =
        dataDiffQueries
            .selectAll()
            .asFlow()
            .mapToList()
            .filterPrevious()
            .mapNotNull { dataDiffList ->
                userInfoGateway
                    .getCurrentUserOrNull()
                    ?.id
                    ?.let { dataDiffList.mapToEntityForUser(it) }
            }
            .filter { it.isNotEmpty() }

    private fun List<DataDiffDb>.mapToEntityForUser(userId: Int): List<DataDiffEntity> =
        filter { it.userId == userId }
            .groupBy { BasenameDtoMapper.toEntity(it.basename) }
            .map { (basename, dbDiffList) ->
                getDataDiff(userId, basename, dbDiffList)
            }
            .filterNot { it.updatedIds.isEmpty() && it.deletedIds.isEmpty() }

    private fun getDataDiff(
        userId: Int,
        basename: Basename,
        dataDiffList: List<DataDiffDb>
    ): DataDiffEntity =
        DataDiffDtoMapper.toEntity(
            userId = userId,
            basename = basename,
            updated = dataDiffList
                .map { dataDiff -> selectUpdatedEntityList(dataDiff.id) }
                .flatten(),
            deleted = dataDiffList
                .map { dataDiff -> selectDeletedEntityList(dataDiff.id) }
                .flatten()
        )

    private fun selectUpdatedEntityList(dataDiffId: Int): List<UpdatedEntityDb> =
        updatedEntityQueries
            .selectByDataDiffId(dataDiffId)
            .executeAsList()

    private fun selectDeletedEntityList(dataDiffId: Int): List<DeletedEntityDb> =
        deletedEntityQueries
            .selectByDataDiffId(dataDiffId)
            .executeAsList()
    
    override suspend fun delete(list: List<DataDiffEntity>) {
        for (diff in list) {
            deleteBasenameData(diff.basename, diff.deletedIds)
            dataDiffQueries
                .selectIds(BasenameDtoMapper.value(diff.basename), diff.userId)
                .executeAsList()
                .forEach { diffId ->
                    deletedEntityQueries.deleteByDataDiffId(diffId)
                    updatedEntityQueries.deleteByDataDiffId(diffId)
                    dataDiffQueries.deleteById(diffId)
                }
        }
    }

    private fun deleteBasenameData(basename: Basename, deletedIds: List<Int>) {
        if (deletedIds.isNotEmpty()) {
            val deleteById = when (basename) {
                Basename.Subscription -> subscriptionQueries::deleteById
                Basename.Timetable -> timetableQueries::deleteById
                Basename.Lecturer -> lecturerQueries::deleteById
                Basename.Class -> classQueries::deleteById
                Basename.UniversityInfo -> universityInfoQueries::deleteById
            }
            for (id in deletedIds) deleteById(id)
        }
    }

    override suspend fun diff(session: Session): Either<DataFailure, Pair<Timestamp, List<Basename>>> =
        updateDataNetworkClient
            .diff(session)
            .map { diffResponse ->
                val basenameList = diffResponse
                    .basenameList
                    .map(BasenameDtoMapper::toEntity)
                Timestamp(diffResponse.timestamp) to basenameList
            }

    override suspend fun updateData(
        session: Session,
        basename: Basename,
        availableDiff: DataDiffEntity?
    ): Either<DataFailure, Unit> {
        val existsIds = availableDiff?.let { it.updatedIds + it.deletedIds } ?: emptyList()
        return updateDataNetworkClient
            .sync(
                basename = BasenameDtoMapper.value(basename),
                existsIds = existsIds,
                session = session
            )
            .flatMap { (updatedIds, deletedIds) ->
                deleteBasenameData(basename, deletedIds)
                meta(session, basename, updatedIds)
            }
    }

    override suspend fun meta(
        session: Session,
        basename: Basename,
        updatedIds: List<Int>
    ): Either<DataFailure, Unit> =
        if (updatedIds.isNotEmpty())
            updateDataNetworkClient
                .meta(
                    session = session,
                    basename = BasenameDtoMapper.value(basename),
                    basenameSerializer = getSerializer(basename),
                    updatedIds = updatedIds
                )
                .handle(basename)
        else
            Either.right(Unit)

    private fun getSerializer(basename: Basename): KSerializer<*> =
        when (basename) {
            Basename.Subscription -> SubscriptionNetworkDto.serializer()
            Basename.Timetable -> TimetableNetworkDto.serializer()
            Basename.Class -> ClassNetworkDto.serializer()
            Basename.Lecturer -> LecturerNetworkDto.serializer()
            Basename.UniversityInfo -> FantasticFour.serializer()
        }

    @Suppress("UNCHECKED_CAST")
    private suspend fun Either<DataFailure, List<*>>.handle(basename: Basename): Either<DataFailure, Unit> =
        flatMap {
            when (basename) {
                Basename.Subscription -> handleSubscriptionList(it as List<SubscriptionNetworkDto>)
                Basename.Timetable -> handleTimetableList(it as List<TimetableNetworkDto>)
                Basename.Class -> handleClassList(it as List<ClassNetworkDto>)
                Basename.Lecturer -> handleLecturerList(it as List<LecturerNetworkDto>)
                Basename.UniversityInfo -> handleUniversityInfoList(it as List<FantasticFour>)
            }
        }

    private suspend fun handleSubscriptionList(
        list: List<SubscriptionNetworkDto>
    ): Either<DataFailure, Unit> {
        for (networkDto in list)
            subscriptionQueries.update(SubscriptionDtoMapper.toDbDto(networkDto))
        return appInfoGateway.checkSubscriptionDependencies(list)
    }

    private suspend fun handleTimetableList(
        list: List<TimetableNetworkDto>
    ): Either<DataFailure, Unit> {
        for (timetable in list)
            timetableQueries.update(TimetableDtoMapper.toDbDto(timetable))
        return appInfoGateway.checkTimetableDependencies(list)
    }

    private suspend fun handleClassList(list: List<ClassNetworkDto>): Either<DataFailure, Unit> {
        for (`class` in list)
            classQueries.update(ClassDtoMapper.toDbDto(`class`))
        return appInfoGateway.checkClassDependencies(list)
    }

    private fun handleLecturerList(list: List<LecturerNetworkDto>): Either<DataFailure, Unit> {
        for (lecturer in list)
            lecturerQueries.update(LecturerDtoMapper.toDbDto(lecturer))
        return Either.right(Unit)
    }

    private fun handleUniversityInfoList(list: List<FantasticFour>): Either<DataFailure, Unit> {
        for (info in list.map(::UniversityInfoNetworkDto))
            universityInfoQueries.update(UniversityInfoDtoMapper.toDbDto(info))
        return Either.right(Unit)
    }
}