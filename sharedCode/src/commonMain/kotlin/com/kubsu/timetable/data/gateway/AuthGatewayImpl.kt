package com.kubsu.timetable.data.gateway

import com.kubsu.timetable.AuthFail
import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.WrapperFailure
import com.kubsu.timetable.data.db.diff.DataDiffQueries
import com.kubsu.timetable.data.db.diff.DeletedEntityQueries
import com.kubsu.timetable.data.db.diff.UpdatedEntityQueries
import com.kubsu.timetable.data.db.timetable.*
import com.kubsu.timetable.data.mapper.UserMapper
import com.kubsu.timetable.data.network.client.user.UserInfoNetworkClient
import com.kubsu.timetable.data.storage.user.UserStorage
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.interactor.auth.AuthGateway

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
    private val userStorage: UserStorage
) : AuthGateway {
    override suspend fun signIn(
        email: String,
        password: String
    ): Either<WrapperFailure<AuthFail>, Unit> =
        networkClient
            .signIn(email, password)
            .map {
                userStorage.set(UserMapper.toStorageDto(it, Timestamp.create()))
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