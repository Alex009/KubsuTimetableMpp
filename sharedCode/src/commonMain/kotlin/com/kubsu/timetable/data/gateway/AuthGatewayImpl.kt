package com.kubsu.timetable.data.gateway

import com.egroden.teaco.Either
import com.egroden.teaco.bimap
import com.egroden.teaco.flatMap
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.data.db.diff.DataDiffQueries
import com.kubsu.timetable.data.db.diff.DeletedEntityQueries
import com.kubsu.timetable.data.db.diff.UpdatedEntityQueries
import com.kubsu.timetable.data.mapper.UserDtoMapper
import com.kubsu.timetable.data.network.client.user.UserInfoNetworkClient
import com.kubsu.timetable.data.network.dto.UserData
import com.kubsu.timetable.data.storage.user.info.UserStorage
import com.kubsu.timetable.data.storage.user.session.Session
import com.kubsu.timetable.data.storage.user.session.SessionStorage
import com.kubsu.timetable.data.storage.user.token.DeliveredToken
import com.kubsu.timetable.data.storage.user.token.Token
import com.kubsu.timetable.data.storage.user.token.TokenStorage
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.interactor.auth.AuthGateway
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AuthGatewayImpl(
    private val dataDiffQueries: DataDiffQueries,
    private val updatedEntityQueries: UpdatedEntityQueries,
    private val deletedEntityQueries: DeletedEntityQueries,
    private val userInfoNetworkClient: UserInfoNetworkClient,
    private val userStorage: UserStorage,
    private val tokenStorage: TokenStorage,
    private val sessionStorage: SessionStorage
) : AuthGateway {
    override suspend fun signInTransaction(
        email: String,
        password: String,
        token: Token?,
        withTransaction: suspend (UserData) -> Either<DataFailure, Unit>
    ): Either<RequestFailure<List<SignInFail>>, UserEntity> =
        userInfoNetworkClient
            .signIn(email, password, token)
            .flatMap { userData ->
                withTransaction(userData).bimap(
                    leftOperation = { RequestFailure<List<SignInFail>>(it) },
                    rightOperation = {
                        sessionStorage.set(userData.session)
                        userStorage.set(UserDtoMapper.toStorageDto(userData.user))
                        tokenStorage.set(token?.value?.let(::DeliveredToken))
                        return@bimap UserDtoMapper.toEntity(userData.user)
                    }
                )
            }

    override suspend fun registrationUser(
        email: String,
        password: String
    ): Either<RequestFailure<List<UserInfoFail>>, Unit> =
        userInfoNetworkClient.registration(email, password)

    override suspend fun logout(session: Session) {
        deleteUserInfo()
        GlobalScope.launch {
            userInfoNetworkClient.logout(session)
        }
    }

    private fun deleteUserInfo() {
        dataDiffQueries.deleteAll()
        updatedEntityQueries.deleteAll()
        deletedEntityQueries.deleteAll()
        userStorage.set(null)
        sessionStorage.set(null)
    }
}