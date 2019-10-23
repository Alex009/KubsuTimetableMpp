package com.kubsu.timetable.data.gateway

import com.egroden.teaco.Either
import com.egroden.teaco.flatMap
import com.egroden.teaco.map
import com.egroden.teaco.mapLeft
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.data.mapper.UserDtoMapper
import com.kubsu.timetable.data.network.client.user.UserInfoNetworkClient
import com.kubsu.timetable.data.storage.user.info.UserStorage
import com.kubsu.timetable.data.storage.user.session.SessionDto
import com.kubsu.timetable.data.storage.user.session.SessionStorage
import com.kubsu.timetable.data.storage.user.session.getEitherFailure
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway

class UserInfoGatewayImpl(
    private val networkClient: UserInfoNetworkClient,
    private val userStorage: UserStorage,
    private val sessionStorage: SessionStorage
) : UserInfoGateway {
    override suspend fun getCurrentUserOrNull(): UserEntity? =
        userStorage
            .get()
            ?.let(UserDtoMapper::toEntity)

    override suspend fun updateUserInfo(
        user: UserEntity
    ): Either<RequestFailure<List<UserInfoFail>>, Unit> =
        sessionStorage
            .getEitherFailure()
            .mapLeft { RequestFailure<List<UserInfoFail>>(it) }
            .flatMap { session ->
                networkClient
                    .update(session, UserDtoMapper.toNetworkDto(user))
                    .map {
                        userStorage.set(UserDtoMapper.toStorageDto(user))
                    }
            }

    override suspend fun updateTimestamp(timestamp: Timestamp): Either<DataFailure, Unit> =
        sessionStorage
            .getEitherFailure()
            .map { session ->
                sessionStorage.set(
                    SessionDto(
                        id = session.id,
                        timestamp = timestamp
                    )
                )
            }
}