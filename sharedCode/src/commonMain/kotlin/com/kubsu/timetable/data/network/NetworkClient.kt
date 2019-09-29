package com.kubsu.timetable.data.network

import arrow.core.Either
import com.kubsu.timetable.AuthFail
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.WrapperFailure
import com.kubsu.timetable.data.network.dto.BasenameNetworkDto
import com.kubsu.timetable.data.network.dto.MainInfoNetworkDto
import com.kubsu.timetable.data.network.dto.UserNetworkDto
import com.kubsu.timetable.data.network.dto.data.ClassNetworkDto
import com.kubsu.timetable.data.network.dto.data.LecturerNetworkDto
import com.kubsu.timetable.data.network.dto.data.SubscriptionNetworkDto
import com.kubsu.timetable.data.network.dto.data.TimetableNetworkDto
import com.kubsu.timetable.data.network.dto.select.FacultyNetworkDto
import com.kubsu.timetable.data.network.dto.select.GroupNetworkDto
import com.kubsu.timetable.data.network.dto.select.OccupationNetworkDto
import com.kubsu.timetable.data.network.dto.select.SubgroupNetworkDto
import com.kubsu.timetable.data.network.response.DiffResponse
import com.kubsu.timetable.data.network.response.SyncResponse
import com.kubsu.timetable.domain.entity.Timestamp

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
    suspend fun selectClassTimeById(id: Int): Either<NetworkFailure, ClassNetworkDto>

    // Update
    suspend fun diff(timestamp: Timestamp): Either<NetworkFailure, DiffResponse>
    suspend fun sync(basename: BasenameNetworkDto, existsIds: List<Int>): Either<NetworkFailure, SyncResponse>
    suspend fun <T> meta(basename: BasenameNetworkDto, updatedIds: List<Int>): Either<NetworkFailure, List<T>>
}