package com.kubsu.timetable.domain.interactor.appinfo

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.SubscriptionNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto
import com.kubsu.timetable.data.storage.user.session.Session

interface AppInfoGateway {
    suspend fun updateInfo(session: Session, userId: Int): Either<DataFailure, Unit>

    suspend fun checkSubscriptionDependencies(
        list: List<SubscriptionNetworkDto>
    ): Either<DataFailure, Unit>

    suspend fun checkTimetableDependencies(
        list: List<TimetableNetworkDto>
    ): Either<DataFailure, Unit>

    suspend fun checkClassDependencies(
        list: List<ClassNetworkDto>
    ): Either<DataFailure, Unit>

    suspend fun clearUserInfo(userId: Int)
}