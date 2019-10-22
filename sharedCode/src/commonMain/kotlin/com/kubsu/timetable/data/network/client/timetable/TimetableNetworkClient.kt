package com.kubsu.timetable.data.network.client.timetable

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.ClassTimeNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.LecturerNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto

interface TimetableNetworkClient {
    suspend fun selectTimetableList(subgroupId: Int): Either<DataFailure, List<TimetableNetworkDto>>
    suspend fun selectClassesByTimetableId(timetableId: Int): Either<DataFailure, List<ClassNetworkDto>>
    suspend fun selectLecturerById(id: Int): Either<DataFailure, LecturerNetworkDto>
    suspend fun selectClassTimeById(id: Int): Either<DataFailure, ClassTimeNetworkDto>
}