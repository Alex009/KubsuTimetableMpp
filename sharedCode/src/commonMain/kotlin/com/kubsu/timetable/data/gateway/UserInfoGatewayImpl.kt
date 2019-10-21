package com.kubsu.timetable.data.gateway

import com.kubsu.timetable.Either
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.data.mapper.UserDtoMapper
import com.kubsu.timetable.data.network.client.user.UserInfoNetworkClient
import com.kubsu.timetable.data.storage.user.UserStorage
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway

class UserInfoGatewayImpl(
    private val networkClient: UserInfoNetworkClient,
    private val userStorage: UserStorage
) : UserInfoGateway {
    override suspend fun getCurrentUserOrNull(): UserEntity? =
        userStorage
            .get()
            ?.let(UserDtoMapper::toEntity)

    override suspend fun updateUserInfo(
        user: UserEntity
    ): Either<RequestFailure<List<UserInfoFail>>, Unit> =
        networkClient
            .update(UserDtoMapper.toNetworkDto(user))
            .map {
                userStorage.set(UserDtoMapper.toStorageDto(user))
            }

    override suspend fun updateTimestamp(user: UserEntity, timestamp: Timestamp) =
        userStorage.set(
            UserDtoMapper
                .toStorageDto(user)
                .copy(timestamp = timestamp.value)
        )
}