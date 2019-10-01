package com.kubsu.timetable.domain.interactor.timetable

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity

interface TimetableInteractor {
    suspend fun getAllTimetables(subscription: SubscriptionEntity): Either<NetworkFailure, List<TimetableEntity>>
}