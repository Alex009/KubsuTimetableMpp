package com.kubsu.timetable.data.network.client.timetable

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.ClassTimeNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.LecturerNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto

class TimetableNetworkClientImpl : TimetableNetworkClient {
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
}