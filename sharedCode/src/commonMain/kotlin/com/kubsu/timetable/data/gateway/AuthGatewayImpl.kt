package com.kubsu.timetable.data.gateway

import com.egroden.teaco.Either
import com.egroden.teaco.map
import com.kubsu.timetable.RequestFailure
import com.kubsu.timetable.SignInFail
import com.kubsu.timetable.UserInfoFail
import com.kubsu.timetable.data.db.diff.DataDiffQueries
import com.kubsu.timetable.data.db.diff.DeletedEntityQueries
import com.kubsu.timetable.data.db.diff.UpdatedEntityQueries
import com.kubsu.timetable.data.db.timetable.*
import com.kubsu.timetable.data.mapper.UserDtoMapper
import com.kubsu.timetable.data.network.client.user.UserInfoNetworkClient
import com.kubsu.timetable.data.storage.user.info.UserStorage
import com.kubsu.timetable.data.storage.user.session.SessionStorage
import com.kubsu.timetable.data.storage.user.token.DeliveredToken
import com.kubsu.timetable.data.storage.user.token.Token
import com.kubsu.timetable.data.storage.user.token.TokenStorage
import com.kubsu.timetable.domain.interactor.auth.AuthGateway
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AuthGatewayImpl(
    private val classQueries: ClassQueries,
    private val classTimeQueries: ClassTimeQueries,
    private val lecturerQueries: LecturerQueries,
    private val subscriptionQueries: SubscriptionQueries,
    private val timetableQueries: TimetableQueries,
    private val dataDiffQueries: DataDiffQueries,
    private val updatedEntityQueries: UpdatedEntityQueries,
    private val deletedEntityQueries: DeletedEntityQueries,
    private val networkClient: UserInfoNetworkClient,
    private val userStorage: UserStorage,
    private val sessionStorage: SessionStorage,
    private val tokenStorage: TokenStorage
) : AuthGateway {
    override suspend fun signIn(
        email: String,
        password: String,
        token: Token?
    ): Either<RequestFailure<List<SignInFail>>, Unit> =
        networkClient
            .signIn(email, password, token)
            .map {
                userStorage.set(UserDtoMapper.toStorageDto(it.user))
                sessionStorage.set(it.session)
                tokenStorage.set(token?.value?.let(::DeliveredToken))
            }

    override suspend fun registrationUser(
        email: String,
        password: String
    ): Either<RequestFailure<List<UserInfoFail>>, Unit> =
        networkClient.registration(email, password)

    override suspend fun logout() {
        clearDatabase()
        userStorage.set(null)
        sessionStorage.get()?.let {
            sessionStorage.set(null)
            GlobalScope.launch {
                networkClient.logout(it)
            }
        }
    }

    private fun clearDatabase() {
        classQueries.deleteAll()
        classTimeQueries.deleteAll()
        lecturerQueries.deleteAll()
        subscriptionQueries.deleteAll()
        timetableQueries.deleteAll()
        dataDiffQueries.deleteAll()
        updatedEntityQueries.deleteAll()
        deletedEntityQueries.deleteAll()
    }
}