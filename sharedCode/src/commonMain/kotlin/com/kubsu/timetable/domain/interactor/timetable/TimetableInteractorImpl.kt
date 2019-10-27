package com.kubsu.timetable.domain.interactor.timetable

import com.egroden.teaco.Either
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.domain.entity.UserEntity
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.domain.entity.timetable.data.UniversityInfoEntity
import com.kubsu.timetable.extensions.def
import kotlinx.coroutines.flow.Flow

class TimetableInteractorImpl(
    private val timetableGateway: TimetableGateway,
    private val appInfoGateway: AppInfoGateway
) : TimetableInteractor {
    override suspend fun updateInfo(user: UserEntity): Either<DataFailure, Unit> = def {
        appInfoGateway.updateInfo(userId = user.id)
    }

    override fun getUniversityData(timetable: TimetableEntity): Flow<UniversityInfoEntity> =
        timetableGateway.getUniversityData(timetable.facultyId)

    override fun getAllTimetables(
        subscription: SubscriptionEntity
    ): Flow<List<TimetableEntity>> =
        timetableGateway.getAllTimetablesFlow(subscription.subgroupId)
}