package com.kubsu.timetable.data.network

import com.kubsu.timetable.AuthFail
import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.WrapperFailure
import com.kubsu.timetable.data.network.dto.MainInfoNetworkDto
import com.kubsu.timetable.data.network.dto.UserNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.*
import com.kubsu.timetable.data.network.dto.timetable.select.FacultyNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.GroupNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.OccupationNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.SubgroupNetworkDto
import com.kubsu.timetable.data.network.response.DiffResponse
import com.kubsu.timetable.data.network.response.SyncResponse

interface NetworkClient {
    // User info
    suspend fun registration(email: String, password: String): Either<WrapperFailure<AuthFail>, Unit>
    suspend fun signIn(email: String, password: String): Either<WrapperFailure<AuthFail>, UserNetworkDto>
    suspend fun update(user: UserNetworkDto): Either<NetworkFailure, Unit>

    suspend fun selectMainInfo(): Either<NetworkFailure, MainInfoNetworkDto>

    // Create subscription
    suspend fun selectFacultyList(): Either<NetworkFailure, List<FacultyNetworkDto>>
    suspend fun selectOccupationList(facultyId: Int): Either<NetworkFailure, List<OccupationNetworkDto>>
    suspend fun selectGroupList(occupationId: Int): Either<NetworkFailure, List<GroupNetworkDto>>
    suspend fun selectSubgroupList(groupId: Int): Either<NetworkFailure, List<SubgroupNetworkDto>>
    suspend fun createSubscriptionAndReturnId(
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<NetworkFailure, Int>

    // Subscription
    suspend fun selectSubscriptionsForUser(): Either<NetworkFailure, List<SubscriptionNetworkDto>>
    suspend fun selectSubscriptionById(id: Int): Either<NetworkFailure, SubscriptionNetworkDto>
    suspend fun update(subscription: SubscriptionNetworkDto): Either<NetworkFailure, SubscriptionNetworkDto>
    suspend fun deleteSubscription(id: Int): Either<NetworkFailure, SubscriptionNetworkDto>

    // Timetable
    suspend fun selectTimetableListForUser(): Either<NetworkFailure, List<TimetableNetworkDto>>
    suspend fun selectClassesByTimetableId(timetableId: Int): Either<NetworkFailure, List<ClassNetworkDto>>
    suspend fun selectLecturerById(id: Int): Either<NetworkFailure, LecturerNetworkDto>
    suspend fun selectClassTimeById(id: Int): Either<NetworkFailure, ClassTimeNetworkDto>

    // Update
    suspend fun diff(timestamp: Long): Either<NetworkFailure, DiffResponse>

    suspend fun sync(basename: String, existsIds: List<Int>): Either<NetworkFailure, SyncResponse>
    suspend fun <T> meta(basename: String, updatedIds: List<Int>): Either<NetworkFailure, List<T>>
}