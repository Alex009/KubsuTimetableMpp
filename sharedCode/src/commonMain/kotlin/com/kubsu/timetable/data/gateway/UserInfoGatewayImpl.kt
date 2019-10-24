package com.kubsu.timetable.data.gateway

import com.egroden.teaco.*
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.data.mapper.UserDtoMapper
import com.kubsu.timetable.data.network.client.user.UserInfoNetworkClient
import com.kubsu.timetable.data.storage.user.info.UserStorage
import com.kubsu.timetable.data.storage.user.session.SessionDto
import com.kubsu.timetable.data.storage.user.session.SessionStorage
import com.kubsu.timetable.data.storage.user.session.getEitherFailure
import com.kubsu.timetable.data.storage.user.token.TokenDto
import com.kubsu.timetable.data.storage.user.token.TokenStorage
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway

class UserInfoGatewayImpl(
    private val networkClient: UserInfoNetworkClient,
    private val userStorage: UserStorage,
    private val sessionStorage: SessionStorage,
    private val tokenStorage: TokenStorage
) : UserInfoGateway {
    override fun getCurrentUserOrNull(): UserEntity? =
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

    override suspend fun updateToken(token: String): Either<DataFailure, Unit> {
        val newToken = TokenDto(token, delivered = false)
        tokenStorage.set(newToken)
        val currentSession = sessionStorage.get()
        return if (currentSession != null)
            networkClient
                .updateToken(currentSession, newToken)
                .map {
                    tokenStorage.set(newToken.copy(delivered = true))
                }
        else
            Either.right(Unit)
    }

    override fun getCurrentTokenOrNull(): TokenDto? =
        tokenStorage.get()

    override fun updateTimestamp(timestamp: Timestamp): Either<DataFailure, Unit> =
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