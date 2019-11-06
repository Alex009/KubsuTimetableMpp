package com.kubsu.timetable.data.gateway

import com.egroden.teaco.Either
import com.egroden.teaco.left
import com.egroden.teaco.map
import com.egroden.teaco.right
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.data.mapper.UserDtoMapper
import com.kubsu.timetable.data.network.client.user.UserInfoNetworkClient
import com.kubsu.timetable.data.storage.user.info.UserStorage
import com.kubsu.timetable.data.storage.user.session.Session
import com.kubsu.timetable.data.storage.user.session.SessionStorage
import com.kubsu.timetable.data.storage.user.session.getEitherFailure
import com.kubsu.timetable.data.storage.user.token.DeliveredToken
import com.kubsu.timetable.data.storage.user.token.Token
import com.kubsu.timetable.data.storage.user.token.TokenStorage
import com.kubsu.timetable.data.storage.user.token.UndeliveredToken
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway

class UserInfoGatewayImpl(
    private val networkClient: UserInfoNetworkClient,
    private val userStorage: UserStorage,
    private val sessionStorage: SessionStorage,
    private val tokenStorage: TokenStorage
) : UserInfoGateway {
    override fun getCurrentUserEitherFailure(): Either<DataFailure, UserEntity> =
        userStorage
            .get()
            ?.let(UserDtoMapper::toEntity)
            ?.let(Either.Companion::right)
            ?: Either.left(DataFailure.NotAuthenticated("User == null"))

    override suspend fun updateUserInfo(
        session: Session,
        user: UserEntity
    ): Either<RequestFailure<List<UserInfoFail>>, Unit> =
        networkClient
            .update(session, UserDtoMapper.toNetworkDto(user))
            .map {
                userStorage.set(UserDtoMapper.toStorageDto(user))
            }

    override suspend fun updateToken(token: UndeliveredToken): Either<DataFailure, Unit> {
        tokenStorage.set(token)
        val currentSession = sessionStorage.get()
        return if (currentSession != null)
            networkClient
                .updateToken(currentSession, token)
                .map {
                    tokenStorage.set(DeliveredToken(token.value))
                }
        else
            Either.right(Unit)
    }

    override fun getCurrentTokenOrNull(): Token? =
        tokenStorage.get()

    override fun getCurrentSessionEitherFail(): Either<DataFailure.NotAuthenticated, Session> =
        sessionStorage
            .get()
            ?.let(Either.Companion::right)
            ?: Either.left(DataFailure.NotAuthenticated())

    override fun updateTimestamp(timestamp: Timestamp): Either<DataFailure, Unit> =
        sessionStorage
            .getEitherFailure()
            .map { session ->
                sessionStorage.set(
                    Session(id = session.id, timestamp = timestamp)
                )
            }
}