package com.kubsu.timetable.domain.interactor.timetable

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.Either
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.domain.entity.timetable.data.UniversityInfoEntity

interface TimetableInteractor {
    suspend fun getUniversityData(timetable: TimetableEntity): Either<DataFailure, UniversityInfoEntity>
    suspend fun isRelevantForThisWeek(timetable: TimetableEntity): Either<DataFailure, Boolean>
    suspend fun getAllTimetables(subscription: SubscriptionEntity): Either<DataFailure, List<TimetableEntity>>
}