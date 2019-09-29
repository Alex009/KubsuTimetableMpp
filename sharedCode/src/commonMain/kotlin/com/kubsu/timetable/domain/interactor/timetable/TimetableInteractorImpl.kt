package com.kubsu.timetable.domain.interactor.timetable

import com.kubsu.timetable.Either
import com.kubsu.timetable.def
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.domain.entity.MainInfoEntity
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity

class TimetableInteractorImpl(
    private val gateway: TimetableGateway
) : TimetableInteractor {
    override suspend fun getMainInfo(): Either<NetworkFailure, MainInfoEntity> = def {
        gateway.getMainInfo()
    }

    override suspend fun getAllTimetables(
        subscription: SubscriptionEntity
    ): Either<NetworkFailure, List<TimetableEntity>> = def {
        gateway.getAllTimetables(subscription.id)
    }
}