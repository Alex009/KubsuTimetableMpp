package com.kubsu.timetable.domain.interactor.timetable

import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.Either
import com.kubsu.timetable.def
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.domain.entity.timetable.data.UniversityInfoEntity
import com.kubsu.timetable.domain.interactor.userInfo.UserInfoGateway

class TimetableInteractorImpl(
    private val timetableGateway: TimetableGateway,
    private val userInfoGateway: UserInfoGateway
) : TimetableInteractor {
    override suspend fun getUniversityData(
        timetable: TimetableEntity
    ): Either<DataFailure, UniversityInfoEntity> = def {
        timetableGateway.getUniversityData(timetable.facultyId)
    }

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
        val currentUser = userInfoGateway.getCurrentUserOrNull()
        if (currentUser != null)
            timetableGateway.getAll(currentUser, subscription.id)
        else
            Either.left(
                DataFailure.NotAuthenticated("TimetableInteractor#getAllTimetables")
            )
    }
}