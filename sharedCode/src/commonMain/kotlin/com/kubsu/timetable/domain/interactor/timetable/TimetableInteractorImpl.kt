package com.kubsu.timetable.domain.interactor.timetable

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.Either
import com.kubsu.timetable.def
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity

class TimetableInteractorImpl(
    private val timetableGateway: TimetableGateway
) : TimetableInteractor {
    override suspend fun isRelevantForThisWeek(
        timetable: TimetableEntity
    ): Either<DataFailure, Boolean> = def {
        timetableGateway
            .getUniversityData(timetable.facultyId)
            .map { timetable.typeOfWeek == it.typeOfWeek }
    }

    override suspend fun getAllTimetables(
        subscription: SubscriptionEntity
    ): Either<DataFailure, List<TimetableEntity>> = def {
        timetableGateway.getAll(subscription.id)
    }
}