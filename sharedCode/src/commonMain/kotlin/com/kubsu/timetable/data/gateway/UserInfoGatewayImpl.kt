package com.kubsu.timetable.data.gateway

import com.kubsu.timetable.Either
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.UserUpdateFail
import com.kubsu.timetable.data.mapper.UserMapper
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
            ?.let(UserMapper::toEntity)

    override suspend fun updateUserInfo(
        user: UserEntity
    ): Either<RequestFailure<List<UserUpdateFail>>, Unit> =
        networkClient
            .update(UserMapper.toNetworkDto(user))
            .map {
                userStorage.set(UserMapper.toStorageDto(user))
            }

    override suspend fun updateTimestamp(user: UserEntity, timestamp: Timestamp) =
        userStorage.set(
            UserMapper
                .toStorageDto(user)
                .copy(timestamp = timestamp.value)
        )
}