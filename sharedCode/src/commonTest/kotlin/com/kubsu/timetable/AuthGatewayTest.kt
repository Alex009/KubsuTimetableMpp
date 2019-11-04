package com.kubsu.timetable

import com.egroden.teaco.*
import com.kubsu.timetable.data.db.diff.DataDiffQueries
import com.kubsu.timetable.data.db.diff.DeletedEntityQueries
import com.kubsu.timetable.data.db.diff.UpdatedEntityQueries
import com.kubsu.timetable.data.gateway.AuthGatewayImpl
import com.kubsu.timetable.data.mapper.UserDtoMapper
import com.kubsu.timetable.data.network.client.user.UserInfoNetworkClient
import com.kubsu.timetable.data.network.dto.UserData
import com.kubsu.timetable.data.network.dto.UserNetworkDto
import com.kubsu.timetable.data.storage.user.info.UserStorage
import com.kubsu.timetable.data.storage.user.session.Session
import com.kubsu.timetable.data.storage.user.session.SessionStorage
import com.kubsu.timetable.data.storage.user.token.TokenStorage
import com.kubsu.timetable.data.storage.user.token.UndeliveredToken
import com.kubsu.timetable.domain.interactor.auth.AuthGateway
import io.mockk.*
import runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthGatewayTest {
    private val userInfoNetworkClient = mockk<UserInfoNetworkClient>()
    private val dataDiffQueries = mockk<DataDiffQueries>()
    private val updatedEntityQueries = mockk<UpdatedEntityQueries>()
    private val deletedEntityQueries = mockk<DeletedEntityQueries>()
    private val userStorage = mockk<UserStorage>()
    private val tokenStorage = mockk<TokenStorage>()
    private val sessionStorage = mockk<SessionStorage>()

    private val authGateway: AuthGateway = AuthGatewayImpl(
        userInfoNetworkClient = userInfoNetworkClient,
        dataDiffQueries = dataDiffQueries,
        updatedEntityQueries = updatedEntityQueries,
        deletedEntityQueries = deletedEntityQueries,
        userStorage = userStorage,
        tokenStorage = tokenStorage,
        sessionStorage = sessionStorage
    )

    private val email = ""
    private val password = ""
    private val session = mockk<Session>()
    private val token = UndeliveredToken("")
    private val userData = UserData(
        user = UserNetworkDto(0, email, "", ""),
        session = session
    )
    private val user = UserDtoMapper.toStorageDto(userData.user)

    @BeforeTest
    fun before() {
        every { dataDiffQueries.deleteAll() } returns Unit
        every { updatedEntityQueries.deleteAll() } returns Unit
        every { deletedEntityQueries.deleteAll() } returns Unit

        every { userStorage.set(null) } returns Unit
        every { userStorage.set(user) } returns Unit

        every { sessionStorage.set(null) } returns Unit
        every { sessionStorage.set(session) } returns Unit

        coEvery { tokenStorage.set(any()) } returns Unit
    }

    @Test
    fun signInSuccess() = runTest {
        coEvery {
            userInfoNetworkClient.signIn(email, password, token)
        } returns Either.right(userData)

        val userDto = UserDtoMapper.toEntity(userData.user)
        authGateway
            .signInTransaction(email, password, token,
                withTransaction = { Either.right(Unit) }
            )
            .fold(
                ifLeft = { throw IllegalStateException() },
                ifRight = { assertEquals(userDto, it) }
            )

        verify { sessionStorage.set(session) }
        verify { userStorage.set(user) }
        verify { tokenStorage.set(any()) }
        confirmVerified(sessionStorage, userStorage, tokenStorage)
    }

    @Test
    fun signInRequestFailure() = runTest {
        coEvery {
            userInfoNetworkClient.signIn(email, password, token)
        } returns Either.left(RequestFailure())

        authGateway
            .signInTransaction(email, password, token,
                withTransaction = { Either.right(Unit) }
            )
            .map {
                throw IllegalStateException()
            }

        verify(inverse = true) { sessionStorage.set(session) }
        verify(inverse = true) { userStorage.set(user) }
        verify(inverse = true) { tokenStorage.set(any()) }
        confirmVerified(sessionStorage, userStorage, tokenStorage)
    }

    @Test
    fun signInTransactionFailure() = runTest {
        coEvery {
            userInfoNetworkClient.signIn(email, password, token)
        } returns Either.right(userData)

        authGateway
            .signInTransaction(email, password, token,
                withTransaction = { Either.left(DataFailure.ConnectionToRepository("")) }
            )
            .map {
                throw IllegalStateException()
            }

        verify(inverse = true) { sessionStorage.set(session) }
        verify(inverse = true) { userStorage.set(user) }
        verify(inverse = true) { tokenStorage.set(any()) }
        confirmVerified(sessionStorage, userStorage, tokenStorage)
    }

    @Test
    fun registrationSuccess() = runTest {
        coEvery { userInfoNetworkClient.registration(email, password) } returns Either.right(Unit)

        authGateway
            .registrationUser(email, password)
            .mapLeft { throw IllegalStateException() }

        coVerify { userInfoNetworkClient.registration(email, password) }
        confirmVerified(userInfoNetworkClient)
    }

    @Test
    fun registrationFail() = runTest {
        coEvery {
            userInfoNetworkClient.registration(email, password)
        } returns Either.left(RequestFailure())

        authGateway
            .registrationUser(email, password)
            .map { throw IllegalStateException() }

        coVerify { userInfoNetworkClient.registration(email, password) }
        confirmVerified(userInfoNetworkClient)
    }

    @Test
    fun logoutSuccess() = runTest {
        coEvery { userInfoNetworkClient.logout(session) } returns Either.right(Unit)

        authGateway.logout(session)

        verify { dataDiffQueries.deleteAll() }
        verify { updatedEntityQueries.deleteAll() }
        verify { deletedEntityQueries.deleteAll() }
        verify { userStorage.set(null) }
        verify { sessionStorage.set(null) }
        confirmVerified(
            dataDiffQueries,
            updatedEntityQueries,
            deletedEntityQueries,
            userStorage,
            sessionStorage
        )
    }

    @Test
    fun logoutFailure() = runTest {
        coEvery {
            userInfoNetworkClient.logout(session)
        } returns Either.left(DataFailure.ConnectionToRepository(null))

        authGateway.logout(session)

        verify { dataDiffQueries.deleteAll() }
        verify { updatedEntityQueries.deleteAll() }
        verify { deletedEntityQueries.deleteAll() }
        verify { userStorage.set(null) }
        verify { sessionStorage.set(null) }
        confirmVerified(
            dataDiffQueries,
            updatedEntityQueries,
            deletedEntityQueries,
            userStorage,
            sessionStorage
        )
    }
}