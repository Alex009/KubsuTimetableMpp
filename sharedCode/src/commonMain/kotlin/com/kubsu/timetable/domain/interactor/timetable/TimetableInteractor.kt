package com.kubsu.timetable.domain.interactor.timetable

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.domain.entity.timetable.data.UniversityInfoEntity
import kotlinx.coroutines.flow.Flow

interface TimetableInteractor {
    suspend fun updateInfo(user: UserEntity): Either<DataFailure, Unit>

    fun getUniversityData(timetable: TimetableEntity): Flow<UniversityInfoEntity>

    fun getAllTimetables(subscription: SubscriptionEntity): Flow<List<TimetableEntity>>
}