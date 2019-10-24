package com.kubsu.timetable.domain.interactor.timetable

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.domain.entity.timetable.data.UniversityInfoEntity
import kotlinx.coroutines.flow.Flow

interface TimetableInteractor {
    fun getUniversityData(timetable: TimetableEntity): Flow<Either<DataFailure, UniversityInfoEntity>>
    fun getAllTimetables(subscription: SubscriptionEntity): Flow<Either<DataFailure, List<TimetableEntity>>>
}