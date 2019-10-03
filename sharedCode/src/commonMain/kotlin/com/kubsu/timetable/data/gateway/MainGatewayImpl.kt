package com.kubsu.timetable.data.gateway

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.mapper.MainInfoMapper
import com.kubsu.timetable.data.mapper.UserMapper
import com.kubsu.timetable.data.network.client.maininfo.MainInfoNetworkClient
import com.kubsu.timetable.data.network.dto.MainInfoNetworkDto
import com.kubsu.timetable.data.storage.maininfo.MainInfoStorage
import com.kubsu.timetable.data.storage.user.UserStorage
import com.kubsu.timetable.domain.entity.MainInfoEntity
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.interactor.main.MainGateway

class MainGatewayImpl(
    private val mainInfoStorage: MainInfoStorage,
    private val userStorage: UserStorage,
    private val networkClient: MainInfoNetworkClient
) : MainGateway {
    override suspend fun getMainInfo(): Either<NetworkFailure, MainInfoEntity> {
        val mainInfo = mainInfoStorage.get()?.let(MainInfoMapper::toEntity)

        return if (mainInfo != null)
            Either.right(mainInfo)
        else
            networkClient
                .selectMainInfo()
                .map {
                    mainInfoStorage.set(MainInfoMapper.toStorageDto(it))
                    MainInfoMapper.toEntity(it)
                }
    }

    override suspend fun setMainInfoEntity(mainInfo: MainInfoEntity?) =
        mainInfoStorage.set(mainInfo?.let(MainInfoMapper::toStorageDto))

    override suspend fun setMainInfoNetworkDto(mainInfo: MainInfoNetworkDto?) =
        mainInfoStorage.set(mainInfo?.let(MainInfoMapper::toStorageDto))

    override suspend fun getCurrentUser(): UserEntity? =
        userStorage.get()?.let(UserMapper::toEntity)

    override suspend fun set(userEntity: UserEntity?) =
        userStorage.set(userEntity?.let(UserMapper::toStorageDto))
}