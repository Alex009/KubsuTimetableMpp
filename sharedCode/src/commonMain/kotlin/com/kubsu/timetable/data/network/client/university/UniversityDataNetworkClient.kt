package com.kubsu.timetable.data.network.client.university

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.Either
import com.kubsu.timetable.data.network.dto.timetable.data.UniversityInfoNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.FacultyNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.GroupNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.OccupationNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.select.SubgroupNetworkDto

interface UniversityDataNetworkClient {
    suspend fun selectFacultyList(): Either<DataFailure, List<FacultyNetworkDto>>
    suspend fun selectOccupationList(facultyId: Int): Either<DataFailure, List<OccupationNetworkDto>>
    suspend fun selectGroupList(occupationId: Int): Either<DataFailure, List<GroupNetworkDto>>
    suspend fun selectSubgroupList(groupId: Int): Either<DataFailure, List<SubgroupNetworkDto>>
    suspend fun selectUniversityInfo(facultyId: Int): Either<DataFailure, UniversityInfoNetworkDto>
}