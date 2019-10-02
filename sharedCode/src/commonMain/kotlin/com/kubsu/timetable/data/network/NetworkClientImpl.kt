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

class NetworkClientImpl : NetworkClient {
    override suspend fun signIn(
        email: String,
        password: String
    ): Either<WrapperFailure<AuthFail>, UserNetworkDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun registration(
        email: String,
        password: String
    ): Either<WrapperFailure<AuthFail>, Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update(user: UserNetworkDto): Either<NetworkFailure, Unit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun selectMainInfo(): Either<NetworkFailure, MainInfoNetworkDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun selectFacultyList(): Either<NetworkFailure, List<FacultyNetworkDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun selectOccupationList(facultyId: Int): Either<NetworkFailure, List<OccupationNetworkDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun selectGroupList(occupationId: Int): Either<NetworkFailure, List<GroupNetworkDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun selectSubgroupList(groupId: Int): Either<NetworkFailure, List<SubgroupNetworkDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun createSubscriptionAndReturnId(
        subgroupId: Int,
        subscriptionName: String,
        isMain: Boolean
    ): Either<NetworkFailure, Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun selectSubscriptionsForUser(): Either<NetworkFailure, List<SubscriptionNetworkDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun selectSubscriptionById(id: Int): Either<NetworkFailure, SubscriptionNetworkDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun update(subscription: SubscriptionNetworkDto): Either<NetworkFailure, SubscriptionNetworkDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteSubscription(id: Int): Either<NetworkFailure, SubscriptionNetworkDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun selectTimetableListForUser(): Either<NetworkFailure, List<TimetableNetworkDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun selectClassesByTimetableId(timetableId: Int): Either<NetworkFailure, List<ClassNetworkDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun selectLecturerById(id: Int): Either<NetworkFailure, LecturerNetworkDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun selectClassTimeById(id: Int): Either<NetworkFailure, ClassTimeNetworkDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun diff(timestamp: Long): Either<NetworkFailure, DiffResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun syncSubscription(
        timestamp: Long,
        existsIds: List<Int>
    ): Either<NetworkFailure, SyncResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun syncTimetable(
        timestamp: Long,
        existsIds: List<Int>
    ): Either<NetworkFailure, SyncResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun syncLecturer(
        timestamp: Long,
        existsIds: List<Int>
    ): Either<NetworkFailure, SyncResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun syncClass(
        timestamp: Long,
        existsIds: List<Int>
    ): Either<NetworkFailure, SyncResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun syncMainInfo(
        timestamp: Long,
        existsIds: List<Int>
    ): Either<NetworkFailure, SyncResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun metaSubscription(updatedIds: List<Int>): Either<NetworkFailure, List<SubscriptionNetworkDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun metaTimetable(updatedIds: List<Int>): Either<NetworkFailure, List<TimetableNetworkDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun metaLecturer(updatedIds: List<Int>): Either<NetworkFailure, List<LecturerNetworkDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun metaClass(updatedIds: List<Int>): Either<NetworkFailure, List<ClassNetworkDto>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun metaMainInfo(updatedId: Int): Either<NetworkFailure, MainInfoNetworkDto> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}