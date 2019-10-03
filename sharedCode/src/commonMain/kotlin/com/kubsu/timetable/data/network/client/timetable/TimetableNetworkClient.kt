package com.kubsu.timetable.data.network.client.timetable

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.ClassTimeNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.LecturerNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto

interface TimetableNetworkClient {
    suspend fun selectTimetableListForUser(): Either<NetworkFailure, List<TimetableNetworkDto>>
    suspend fun selectClassesByTimetableId(timetableId: Int): Either<NetworkFailure, List<ClassNetworkDto>>
    suspend fun selectLecturerById(id: Int): Either<NetworkFailure, LecturerNetworkDto>
    suspend fun selectClassTimeById(id: Int): Either<NetworkFailure, ClassTimeNetworkDto>
}