package com.kubsu.timetable.data.network.client.update

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.network.dto.MainInfoNetworkDto
import com.kubsu.timetable.data.network.dto.response.DiffResponse
import com.kubsu.timetable.data.network.dto.response.SyncResponse
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.LecturerNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.SubscriptionNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto

class UpdateDataNetworkClientImpl : UpdateDataNetworkClient {
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