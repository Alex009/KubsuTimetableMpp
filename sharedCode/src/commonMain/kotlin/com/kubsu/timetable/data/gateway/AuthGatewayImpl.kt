package com.kubsu.timetable.data.gateway

import com.kubsu.timetable.AuthFail
import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.WrapperFailure
import com.kubsu.timetable.data.db.timetable.ClassQueries
import com.kubsu.timetable.data.db.timetable.LecturerQueries
import com.kubsu.timetable.data.db.timetable.SubscriptionQueries
import com.kubsu.timetable.data.db.timetable.TimetableQueries
import com.kubsu.timetable.data.mapper.UserMapper
import com.kubsu.timetable.data.network.NetworkClient
import com.kubsu.timetable.data.storage.user.UserStorage
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.entity.createTimestamp
import com.kubsu.timetable.domain.interactor.auth.AuthGateway
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthGatewayImpl(
    private val classQueries: ClassQueries,
    private val lecturerQueries: LecturerQueries,
    private val subscriptionQueries: SubscriptionQueries,
    private val timetableQueries: TimetableQueries,
    private val networkClient: NetworkClient,
    private val userStorage: UserStorage
) : AuthGateway {
    override suspend fun getUserOrNull(): UserEntity? =
        userStorage
            .get()
            ?.let(UserMapper::toEntity)

    override suspend fun signIn(
        email: String,
        password: String
    ): Either<WrapperFailure<AuthFail>, Unit> =
        networkClient
            .signIn(email, password)
            .map {
                userStorage.set(UserMapper.toStorageDto(it, createTimestamp()))
            }

    override suspend fun registrationUser(
        email: String,
        password: String
    ): Either<WrapperFailure<AuthFail>, Unit> =
        networkClient.registration(email, password)

    override suspend fun logout(): Either<NetworkFailure, Unit> {
        if (userStorage.get() != null) {
            userStorage.set(null)
            clearDatabase()
        }
        return Either.right(Unit)
    }

    private suspend fun clearDatabase() = withContext(Dispatchers.Default) {
        classQueries.deleteAll()
        lecturerQueries.deleteAll()
        subscriptionQueries.deleteAll()
        timetableQueries.deleteAll()
    }
}