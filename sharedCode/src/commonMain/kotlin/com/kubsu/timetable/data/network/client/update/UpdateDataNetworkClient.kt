package com.kubsu.timetable.data.network.client.update

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.Either
import com.kubsu.timetable.data.network.dto.response.DiffResponse
import com.kubsu.timetable.data.network.dto.response.SyncResponse
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.LecturerNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.SubscriptionNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto

interface UpdateDataNetworkClient {
    suspend fun diff(timestamp: Long): Either<DataFailure, DiffResponse>

    suspend fun syncSubscription(
        timestamp: Long,
        existsIds: List<Int>
    ): Either<DataFailure, SyncResponse>

    suspend fun syncTimetable(
        timestamp: Long,
        existsIds: List<Int>
    ): Either<DataFailure, SyncResponse>

    suspend fun syncLecturer(
        timestamp: Long,
        existsIds: List<Int>
    ): Either<DataFailure, SyncResponse>

    suspend fun syncClass(
        timestamp: Long,
        existsIds: List<Int>
    ): Either<DataFailure, SyncResponse>

    suspend fun metaSubscription(updatedIds: List<Int>): Either<DataFailure, List<SubscriptionNetworkDto>>
    suspend fun metaTimetable(updatedIds: List<Int>): Either<DataFailure, List<TimetableNetworkDto>>
    suspend fun metaLecturer(updatedIds: List<Int>): Either<DataFailure, List<LecturerNetworkDto>>
    suspend fun metaClass(updatedIds: List<Int>): Either<DataFailure, List<ClassNetworkDto>>
}