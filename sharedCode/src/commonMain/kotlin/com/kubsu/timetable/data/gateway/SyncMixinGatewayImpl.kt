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
import com.kubsu.timetable.data.network.dto.diff.FantasticFour
import com.kubsu.timetable.data.network.dto.timetable.data.*
import com.kubsu.timetable.data.storage.user.info.UserStorage
import com.kubsu.timetable.data.storage.user.session.SessionStorage
import com.kubsu.timetable.data.storage.user.session.getEitherFailure
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import com.kubsu.timetable.domain.interactor.sync.SyncMixinGateway
import com.kubsu.timetable.extensions.getContentFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    private val networkClient: UpdateDataNetworkClient,
    private val userStorage: UserStorage,
    private val sessionStorage: SessionStorage
) : SyncMixinGateway {
    override suspend fun registerDataDiff(entity: DataDiffEntity) {
        val id = getIdForDataDiff(entity)
        for (updatedId in entity.updatedIds)
            updatedEntityQueries.update(id, updatedId)
        for (deletedId in entity.deletedIds)
            deletedEntityQueries.update(id, deletedId)
    }

    private fun getIdForDataDiff(dataDiff: DataDiffEntity): Int {
        val basenameStr = BasenameDtoMapper.value(dataDiff.basename)
        return dataDiffQueries
            .selectId(basenameStr, dataDiff.userId)
            .executeAsOneOrNull()
            ?: createAndGetId(basenameStr, dataDiff.userId)
    }

    private fun createAndGetId(basename: String, userId: Int): Int {
        dataDiffQueries.update(basename, userId)
        return dataDiffQueries
            .selectId(basename, userId)
            .executeAsOne()
    }

    @UseExperimental(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override fun getAvailableDiffListFlowForCurrentUser(): Flow<List<DataDiffEntity>> =
        dataDiffQueries
            .selectAll()
            .getContentFlow { query -> query.executeAsList() }
            .mapNotNull { dataDiffList ->
                val userId = userStorage.get()?.id ?: return@mapNotNull null
                dataDiffList to userId
            }
            .map { (dataDiffList, userId) ->
                Basename
                    .list
                    .flatMap { basename ->
                        getIdsList(userId, basename, dataDiffList)
                    }
            }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    private fun getIdsList(
        userId: Int,
        basename: Basename,
        dataDiffList: List<DataDiffDb>
    ): List<DataDiffEntity> {
        val basenameStringify = BasenameDtoMapper.value(basename)
        return dataDiffList
            .filter { it.basename == basenameStringify }
            .map { dataDiff ->
                DataDiffDtoMapper.toEntity(
                    userId = userId,
                    basename = basename,
                    updated = selectUpdatedEntityList(dataDiff.id),
                    deleted = selectDeletedEntityList(dataDiff.id)
                )
            }
    }

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

            val basenameStringify = BasenameDtoMapper.value(diff.basename)
            val diffId = dataDiffQueries
                .selectId(basenameStringify, diff.userId)
                .executeAsOne()
            deletedEntityQueries.deleteByDataDiffId(diffId)
            updatedEntityQueries.deleteByDataDiffId(diffId)
            dataDiffQueries.delete(basenameStringify, diff.userId)
        }
    }

    private fun deleteBasenameData(basename: Basename, deletedIds: List<Int>) {
        val deleteById = when (basename) {
            Basename.Subscription -> subscriptionQueries::deleteById
            Basename.Timetable -> timetableQueries::deleteById
            Basename.Lecturer -> lecturerQueries::deleteById
            Basename.Class -> classQueries::deleteById
            Basename.UniversityInfo -> universityInfoQueries::deleteById
        }
        for (id in deletedIds) deleteById(id)
    }

    override suspend fun diff(): Either<DataFailure, Pair<Timestamp, List<Basename>>> =
        sessionStorage
            .getEitherFailure()
            .flatMap { session ->
                networkClient
                    .diff(session)
                    .map { diffResponse ->
                        val basenameList = diffResponse
                            .basenameList
                            .map(BasenameDtoMapper::toEntity)
                        Timestamp(diffResponse.timestamp) to basenameList
                    }
            }

    override suspend fun updateData(
        basename: Basename,
        availableDiff: DataDiffEntity?
    ): Either<DataFailure, Unit> {
        val existsIds = availableDiff?.let { it.updatedIds + it.deletedIds } ?: emptyList()
        return sessionStorage
            .getEitherFailure()
            .flatMap { session ->
                networkClient
                    .sync(
                        session = session,
                        basename = BasenameDtoMapper.value(basename),
                        existsIds = existsIds
                    )
                    .flatMap { (updatedIds, deletedIds) ->
                        deleteBasenameData(basename, deletedIds)
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
                networkClient
                    .meta(
                        session = session,
                        basename = BasenameDtoMapper.value(basename),
                        basenameSerializer = getSerializer(basename),
                        updatedIds = updatedIds
                    )
                    .handle(basename)
            }

    private fun getSerializer(basename: Basename): KSerializer<*> =
        when (basename) {
            Basename.Subscription -> SubscriptionNetworkDto.serializer()
            Basename.Timetable -> TimetableNetworkDto.serializer()
            Basename.Lecturer -> LecturerNetworkDto.serializer()
            Basename.Class -> ClassNetworkDto.serializer()
            Basename.UniversityInfo -> FantasticFour.serializer()
        }

    @Suppress("UNCHECKED_CAST")
    private fun Either<DataFailure, List<*>>.handle(basename: Basename) = map {
        when (basename) {
            Basename.Subscription -> handleSubscriptionList(it as List<SubscriptionNetworkDto>)
            Basename.Timetable -> handleTimetableList(it as List<TimetableNetworkDto>)
            Basename.Lecturer -> handleLecturerList(it as List<LecturerNetworkDto>)
            Basename.Class -> handleClassList(it as List<ClassNetworkDto>)
            Basename.UniversityInfo -> handleUniversityInfoList(it as List<FantasticFour>)
        }
    }

    private fun handleSubscriptionList(list: List<SubscriptionNetworkDto>) {
        for (networkDto in list)
            subscriptionQueries.update(SubscriptionDtoMapper.toDbDto(networkDto))
    }

    private fun handleTimetableList(list: List<TimetableNetworkDto>) {
        for (timetable in list)
            timetableQueries.update(TimetableDtoMapper.toDbDto(timetable))
    }

    private fun handleLecturerList(list: List<LecturerNetworkDto>) {
        for (lecturer in list)
            lecturerQueries.update(LecturerDtoMapper.toDbDto(lecturer))
    }

    private fun handleClassList(list: List<ClassNetworkDto>) {
        for (`class` in list)
            classQueries.update(ClassDtoMapper.toDbDto(`class`))
    }

    private fun handleUniversityInfoList(list: List<FantasticFour>) {
        for (info in list.map(::UniversityInfoNetworkDto))
            universityInfoQueries.update(UniversityInfoDtoMapper.toDbDto(info))
    }
}