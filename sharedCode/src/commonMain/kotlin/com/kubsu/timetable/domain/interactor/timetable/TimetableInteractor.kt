package com.kubsu.timetable.domain.interactor.timetable

import com.kubsu.timetable.domain.entity.timetable.data.ClassEntity
import com.kubsu.timetable.domain.entity.timetable.data.SubscriptionEntity
import com.kubsu.timetable.domain.entity.timetable.data.TimetableEntity
import com.kubsu.timetable.domain.entity.timetable.data.UniversityInfoEntity
import kotlinx.coroutines.flow.Flow

interface TimetableInteractor {
    fun getUniversityData(timetable: TimetableEntity): Flow<UniversityInfoEntity>

    fun getAllTimetables(subscription: SubscriptionEntity): Flow<List<TimetableEntity>>

    suspend fun changesWasDisplayed(clazz: ClassEntity)
}