package com.kubsu.timetable.domain.interactor.timetable

import com.kubsu.timetable.domain.entity.timetable.data.ClassEntity
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.domain.entity.timetable.data.UniversityInfoEntity
import com.kubsu.timetable.extensions.def
import kotlinx.coroutines.flow.Flow

class TimetableInteractorImpl(
    private val timetableGateway: TimetableGateway
) : TimetableInteractor {
    override fun getUniversityData(timetable: TimetableEntity): Flow<UniversityInfoEntity> =
        timetableGateway.getUniversityData(timetable.facultyId)

    override fun getAllTimetables(
        subscription: SubscriptionEntity
    ): Flow<List<TimetableEntity>> =
        timetableGateway.getAllTimetablesFlow(subscription.subgroupId)

    override suspend fun changesWasDisplayed(clazz: ClassEntity) = def {
        timetableGateway.changesWasDisplayed(clazz)
    }
}