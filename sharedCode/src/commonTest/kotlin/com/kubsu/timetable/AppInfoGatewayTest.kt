package com.kubsu.timetable

import com.egroden.teaco.mapLeft
import com.kubsu.timetable.data.db.timetable.*
import com.kubsu.timetable.data.gateway.AppInfoGatewayImpl
import com.kubsu.timetable.data.network.client.subscription.SubscriptionNetworkClient
import com.kubsu.timetable.data.network.client.timetable.TimetableNetworkClient
import com.kubsu.timetable.data.network.client.university.UniversityDataNetworkClient
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class AppInfoGatewayTest {
    private val classQueries = mockk<ClassQueries>()
    private val classTimeQueries = mockk<ClassTimeQueries>()
    private val lecturerQueries = mockk<LecturerQueries>()
    private val subscriptionQueries = mockk<SubscriptionQueries>()
    private val timetableQueries = mockk<TimetableQueries>()
    private val universityInfoQueries = mockk<UniversityInfoQueries>()
    private val subscriptionNetworkClient = mockk<SubscriptionNetworkClient>()
    private val timetableNetworkClient = mockk<TimetableNetworkClient>()
    private val universityDataNetworkClient = mockk<UniversityDataNetworkClient>()

    private val appInfoGateway = AppInfoGatewayImpl(
        classQueries = classQueries,
        classTimeQueries = classTimeQueries,
        lecturerQueries = lecturerQueries,
        subscriptionQueries = subscriptionQueries,
        timetableQueries = timetableQueries,
        universityInfoQueries = universityInfoQueries,
        subscriptionNetworkClient = subscriptionNetworkClient,
        timetableNetworkClient = timetableNetworkClient,
        universityDataNetworkClient = universityDataNetworkClient
    )

    private val lecturer = LecturerDb.Impl(0, "", "", "")

    @BeforeTest
    fun before() {
    }

    @Test
    fun lecturerExistsInDatabase() = runTest {
        coEvery { lecturerQueries.selectById(lecturer.id).executeAsOneOrNull() } returns lecturer

        appInfoGateway
            .checkAvailabilityOfLecturer(lecturer.id)
            .mapLeft { throw IllegalStateException() }

        coVerify { lecturerQueries.selectById(lecturer.id) }
        coVerify(inverse = true) { timetableNetworkClient.selectLecturerById(lecturer.id) }
        confirmVerified(lecturerQueries, timetableNetworkClient)
    }

    @Test
    fun loadingLecturerSuccess() = runTest {
        coEvery { lecturerQueries.selectById(lecturer.id).executeAsOneOrNull() } returns null
        coEvery { lecturerQueries.update(lecturer) } returns Unit

        appInfoGateway
            .checkAvailabilityOfLecturer(lecturer.id)
            .mapLeft { throw IllegalStateException() }

        coVerify { lecturerQueries.selectById(lecturer.id) }
        coVerify { timetableNetworkClient.selectLecturerById(lecturer.id) }
        confirmVerified(lecturerQueries, timetableNetworkClient)
    }
}