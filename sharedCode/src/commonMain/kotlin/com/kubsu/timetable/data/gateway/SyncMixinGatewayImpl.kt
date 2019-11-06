package com.kubsu.timetable.data.gateway

import com.egroden.teaco.*
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
import com.kubsu.timetable.data.storage.user.token.Token
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import com.kubsu.timetable.domain.interactor.appinfo.AppInfoGateway
import com.kubsu.timetable.domain.interactor.sync.SyncMixinGateway
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway
import com.kubsu.timetable.extensions.filterPrevious
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.KSerializer

class SyncMixinGatewayImpl(
    private val subscriptionQueries: SubscriptionQueries,
    private val timetableQueries: TimetableQueries,
    private val lecturerQueries: LecturerQueries,
    private val classTimeQueries: ClassTimeQueries,
    private val classQueries: ClassQueries,
    private val dataDiffQueries: DataDiffQueries,
    private val universityInfoQueries: UniversityInfoQueries,
    private val updatedEntityQueries: UpdatedEntityQueries,
    private val deletedEntityQueries: DeletedEntityQueries,
    private val updateDataNetworkClient: UpdateDataNetworkClient,
    private val appInfoGateway: AppInfoGateway,
    private val userInfoGateway: UserInfoGateway
) : SyncMixinGateway {
    override suspend fun registerDataDiff(entity: DataDiffEntity.Raw) {
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
        dataDiffQueries.insert(basename, userId, handled = false)
        return dataDiffQueries
            .selectIds(basename, userId)
            .executeAsList()
            .last()
    }

    override suspend fun checkMigrations(
        session: Session,
        token: Token?
    ): Either<DataFailure, Unit> =
        updateDataNetworkClient
            .checkMigrations(session, token)
            .map {
                for (migration in it)
                    meta(
                        session = session,
                        basename = BasenameDtoMapper.toEntity(migration.basename),
                        updatedIds = migration.ids
                    )
            }

    override suspend fun getAvailableDiffList(): List<DataDiffEntity.Merged> =
        userInfoGateway
            .getCurrentUserEitherFailure()
            .fold(
                ifLeft = { emptyList() },
                ifRight = {
                    dataDiffQueries
                        .selectAll()
                        .executeAsList()
                        .mapToEntityForUser(it.id)
                }
            )

    override fun rowDataDiffListFlow(): Flow<List<DataDiffEntity.Raw>> =
        dataDiffQueries
            .selectAll()
            .asFlow()
            .mapToList()
            .filterPrevious()
            .mapNotNull { dataDiffList ->
                userInfoGateway
                    .getCurrentUserEitherFailure()
                    .fold(
                        ifLeft = { null },
                        ifRight = { dataDiffList.mapToEntityForUser(it.id) }
                    )
            }
            .map { mergedList ->
                mergedList
                    .map { it.raw }
                    .filterNot { it.updatedIds.isEmpty() && it.deletedIds.isEmpty() }
            }
            .filter { it.isNotEmpty() }

    private fun List<DataDiffDb>.mapToEntityForUser(userId: Int): List<DataDiffEntity.Merged> =
        filter { it.userId == userId }
            .groupBy { BasenameDtoMapper.toEntity(it.basename) }
            .map { (basename, dbDiffList) ->
                DataDiffDtoMapper.toMergedEntity(
                    userId = userId,
                    basename = basename,
                    dataDiffList = dbDiffList,
                    getUpdated = ::selectUpdatedEntityList,
                    getDeleted = ::selectDeletedEntityList
                )
            }

    private fun selectUpdatedEntityList(dataDiff: DataDiffDb): List<UpdatedEntityDb> =
        updatedEntityQueries
            .selectByDataDiffId(dataDiff.id)
            .executeAsList()

    private fun selectDeletedEntityList(dataDiff: DataDiffDb): List<DeletedEntityDb> =
        deletedEntityQueries
            .selectByDataDiffId(dataDiff.id)
            .executeAsList()

    override suspend fun deleteBasenameData(basename: Basename, deletedIds: List<Int>) {
        if (deletedIds.isNotEmpty()) {
            val deleteById = when (basename) {
                Basename.Subscription -> subscriptionQueries::deleteById
                Basename.Timetable -> timetableQueries::deleteById
                Basename.Lecturer -> lecturerQueries::deleteById
                Basename.Class -> classQueries::deleteById
                Basename.UniversityInfo -> universityInfoQueries::deleteById
                Basename.ClassTime -> classTimeQueries::deleteById
            }
            for (id in deletedIds) deleteById(id)
        }
    }

    override suspend fun delete(list: List<DataDiffEntity.Merged>) {
        for (diff in list)
            dataDiffQueries
                .selectIds(BasenameDtoMapper.value(diff.basename), diff.userId)
                .executeAsList()
                .forEach { diffId ->
                    deletedEntityQueries.deleteByDataDiffId(diffId)
                    updatedEntityQueries.deleteByDataDiffId(diffId)
                    dataDiffQueries.deleteById(diffId)
                }
    }

    override suspend fun rawListHandled(list: List<DataDiffEntity.Raw>) {
        for (raw in list) {
            val basename = BasenameDtoMapper.value(raw.basename)
            dataDiffQueries
                .selectIds(basename, raw.userId)
                .executeAsList()
                .forEach { diffId ->
                    dataDiffQueries.update(
                        id = diffId,
                        basename = basename,
                        userId = raw.userId,
                        handled = true
                    )
                }
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
        availableDiff: DataDiffEntity.Merged?
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
            Basename.ClassTime -> ClassTimeNetworkDto.serializer()
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
                Basename.ClassTime -> handleClassTimeList(it as List<ClassTimeNetworkDto>)
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
            classQueries.update(ClassDtoMapper.toDbDto(`class`, needToEmphasize = true))
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

    private fun handleClassTimeList(list: List<ClassTimeNetworkDto>): Either<DataFailure, Unit> {
        for (classTime in list)
            classTimeQueries.update(ClassTimeDtoMapper.toDbDto(classTime))
        return Either.right(Unit)
    }
}