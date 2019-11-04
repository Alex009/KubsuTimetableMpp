package com.kubsu.timetable

import com.egroden.teaco.*
import com.kubsu.timetable.data.db.timetable.*
import com.kubsu.timetable.data.gateway.AppInfoGatewayImpl
import com.kubsu.timetable.data.mapper.timetable.data.ClassDtoMapper
import com.kubsu.timetable.data.mapper.timetable.data.ClassTimeDtoMapper
import com.kubsu.timetable.data.mapper.timetable.data.LecturerDtoMapper
import com.kubsu.timetable.data.network.client.subscription.SubscriptionNetworkClient
import com.kubsu.timetable.data.network.client.timetable.TimetableNetworkClient
import com.kubsu.timetable.data.network.client.university.UniversityDataNetworkClient
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import io.mockk.*
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

    private val classTime = ClassTimeDb.Impl(0, 0, "", "")
    private val lecturer = LecturerDb.Impl(0, "", "", "")
    private val classNetworkDto = mockk<ClassNetworkDto> {
        every { classTimeId } returns classTime.id
        every { lecturerId } returns lecturer.id
    }

    @BeforeTest fun before() {
    }

    @Test fun classesInDatabase() = runTest {

    }

    @Test fun checkClassDependenciesForEmptyList() = runTest {
        mockkObject(appInfoGateway)
        coEvery { appInfoGateway.checkAvailabilityOfClassTime(any()) } returns Either.right(Unit)
        coEvery { appInfoGateway.checkAvailabilityOfLecturer(any()) } returns Either.right(Unit)

        appInfoGateway
            .checkClassDependencies(emptyList())
            .mapLeft { throw IllegalStateException() }
    }

    @Test fun checkClassDependenciesForNotEmptyList() = runTest {
        mockkObject(ClassDtoMapper)
        mockkObject(appInfoGateway)
        coEvery { appInfoGateway.checkAvailabilityOfClassTime(any()) } returns Either.right(Unit)
        coEvery { appInfoGateway.checkAvailabilityOfLecturer(any()) } returns Either.right(Unit)
        coEvery { ClassDtoMapper.toDbDto(any<ClassNetworkDto>()) } returns mockk()
        coEvery { classQueries.update(any()) } returns Unit

        val list = List<ClassNetworkDto>(10) {
            mockk {
                every { classTimeId } returns it
                every { lecturerId } returns it
            }
        }

        appInfoGateway
            .checkClassDependencies(list)
            .mapLeft { throw IllegalStateException() }
    }

    @Test fun checkClassDependenciesFailure1() = runTest {
        mockkObject(ClassDtoMapper)
        mockkObject(appInfoGateway)
        coEvery {
            appInfoGateway.checkAvailabilityOfClassTime(any())
        } returns Either.left(DataFailure.ConnectionToRepository(""))
        coEvery { appInfoGateway.checkAvailabilityOfLecturer(any()) } returns Either.right(Unit)
        coEvery { ClassDtoMapper.toDbDto(any<ClassNetworkDto>()) } returns mockk()
        coEvery { classQueries.update(any()) } returns Unit

        val list = List<ClassNetworkDto>(10) {
            mockk {
                every { classTimeId } returns it
                every { lecturerId } returns it
            }
        }

        appInfoGateway
            .checkClassDependencies(list)
            .map { throw IllegalStateException() }
    }

    @Test fun checkClassDependenciesFailure2() = runTest {
        mockkObject(ClassDtoMapper)
        mockkObject(appInfoGateway)
        coEvery { appInfoGateway.checkAvailabilityOfClassTime(any()) } returns Either.right(Unit)
        coEvery {
            appInfoGateway.checkAvailabilityOfLecturer(any())
        } returns Either.left(DataFailure.ConnectionToRepository(""))
        coEvery { ClassDtoMapper.toDbDto(any<ClassNetworkDto>()) } returns mockk()
        coEvery { classQueries.update(any()) } returns Unit

        val list = List<ClassNetworkDto>(10) {
            mockk {
                every { classTimeId } returns 0
                every { lecturerId } returns 0
            }
        }

        appInfoGateway
            .checkClassDependencies(list)
            .map { throw IllegalStateException() }
    }

    @Test fun classTimeExistsInDatabase() = runTest {
        every { classTimeQueries.selectById(classTime.id).executeAsOneOrNull() } returns classTime

        appInfoGateway
            .checkAvailabilityOfClassTime(classNetworkDto)
            .mapLeft { throw IllegalStateException() }

        verify { classTimeQueries.selectById(classTime.id) }
        coVerify(inverse = true) { timetableNetworkClient.selectClassTimeById(classTime.id) }
        confirmVerified(classTimeQueries, timetableNetworkClient)
    }

    @Test fun loadingClassTimeSuccess() = runTest {
        every { classTimeQueries.selectById(classTime.id).executeAsOneOrNull() } returns null
        coEvery {
            timetableNetworkClient.selectClassTimeById(classTime.id)
        } returns Either.right(ClassTimeDtoMapper.toNetworkDto(classTime))
        every { classTimeQueries.update(classTime) } returns Unit

        appInfoGateway
            .checkAvailabilityOfClassTime(classNetworkDto)
            .mapLeft { throw IllegalStateException() }

        verify { classTimeQueries.selectById(classTime.id) }
        coVerify { timetableNetworkClient.selectClassTimeById(classTime.id) }
        verify { classTimeQueries.update(any()) }
        confirmVerified(classTimeQueries, timetableNetworkClient)
    }

    @Test fun loadingClassTimeFail() = runTest {
        every { classTimeQueries.selectById(classTime.id).executeAsOneOrNull() } returns null
        coEvery {
            timetableNetworkClient.selectClassTimeById(classTime.id)
        } returns Either.left(DataFailure.ConnectionToRepository(""))
        every { classTimeQueries.update(classTime) } returns Unit

        appInfoGateway
            .checkAvailabilityOfClassTime(classNetworkDto)
            .map { throw IllegalStateException() }

        verify { classTimeQueries.selectById(classTime.id) }
        coVerify { timetableNetworkClient.selectClassTimeById(classTime.id) }
        verify(inverse = true) { classTimeQueries.update(any()) }
        confirmVerified(classTimeQueries, timetableNetworkClient)
    }

    @Test fun lecturerExistsInDatabase() = runTest {
        every { lecturerQueries.selectById(lecturer.id).executeAsOneOrNull() } returns lecturer

        appInfoGateway
            .checkAvailabilityOfLecturer(classNetworkDto)
            .mapLeft { throw IllegalStateException() }

        verify { lecturerQueries.selectById(lecturer.id) }
        coVerify(inverse = true) { timetableNetworkClient.selectLecturerById(lecturer.id) }
        confirmVerified(lecturerQueries, timetableNetworkClient)
    }

    @Test fun loadingLecturerSuccess() = runTest {
        every { lecturerQueries.selectById(lecturer.id).executeAsOneOrNull() } returns null
        coEvery {
            timetableNetworkClient.selectLecturerById(lecturer.id)
        } returns Either.right(LecturerDtoMapper.toNetworkDto(lecturer))
        every { lecturerQueries.update(lecturer) } returns Unit

        appInfoGateway
            .checkAvailabilityOfLecturer(classNetworkDto)
            .mapLeft { throw IllegalStateException() }

        verify { lecturerQueries.selectById(lecturer.id) }
        coVerify { timetableNetworkClient.selectLecturerById(lecturer.id) }
        verify { lecturerQueries.update(any()) }
        confirmVerified(lecturerQueries, timetableNetworkClient)
    }

    @Test fun loadingLecturerFailure() = runTest {
        every { lecturerQueries.selectById(lecturer.id).executeAsOneOrNull() } returns null
        coEvery {
            timetableNetworkClient.selectLecturerById(lecturer.id)
        } returns Either.left(DataFailure.ConnectionToRepository(""))

        appInfoGateway
            .checkAvailabilityOfLecturer(classNetworkDto)
            .map { throw IllegalStateException() }

        verify { lecturerQueries.selectById(lecturer.id) }
        coVerify { timetableNetworkClient.selectLecturerById(lecturer.id) }
        verify(inverse = true) { lecturerQueries.update(any()) }
        confirmVerified(lecturerQueries, timetableNetworkClient)
    }
}