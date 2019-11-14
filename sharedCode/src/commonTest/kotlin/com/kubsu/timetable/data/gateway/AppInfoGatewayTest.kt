package com.kubsu.timetable.data.gateway

import com.egroden.teaco.*
import com.kubsu.timetable.DataFailure
import com.kubsu.timetable.data.db.timetable.*
import com.kubsu.timetable.data.mapper.timetable.data.ClassDtoMapper
import com.kubsu.timetable.data.mapper.timetable.data.ClassTimeDtoMapper
import com.kubsu.timetable.data.mapper.timetable.data.LecturerDtoMapper
import com.kubsu.timetable.data.network.client.subscription.SubscriptionNetworkClient
import com.kubsu.timetable.data.network.client.timetable.TimetableNetworkClient
import com.kubsu.timetable.data.network.client.university.UniversityDataNetworkClient
import com.kubsu.timetable.data.network.dto.timetable.data.ClassNetworkDto
import com.kubsu.timetable.data.network.dto.timetable.data.TimetableNetworkDto
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

    private val appInfoGateway = spyk(
        AppInfoGatewayImpl(
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
    )

    private val timetable = TimetableNetworkDto(0, 0, 0, 0)
    private val classTime = ClassTimeDb.Impl(0, 0, "", "")
    private val lecturer = LecturerDb.Impl(0, "", "", "")
    private val classNetworkDto = ClassNetworkDto(
        id = 0,
        title = "",
        typeOfClass = 0,
        classroom = "",
        classTimeId = classTime.id,
        weekday = 0,
        lecturerId = lecturer.id,
        timetableId = timetable.id
    )

    private val classList = listOf(classNetworkDto)

    @BeforeTest fun before() {
    }

    @Test fun classesInDatabase() = runTest {

    }

    @Test fun checkAvailabilityOfClassesForEmptyList() = runTest {
        every { classQueries.selectByTimetableId(any()).executeAsList() } returns emptyList()
        every { classQueries.update(any()) } returns Unit
        coEvery { timetableNetworkClient.selectClassesByTimetableId(any()) } returns Either.right(
            classList
        )
        coEvery { appInfoGateway.checkClassDependencies(any()) } returns Either.right(Unit)

        appInfoGateway
            .checkAvailabilityOfClasses(timetable)
            .mapLeft { throw IllegalStateException() }

        verify { classQueries.selectByTimetableId(any()).executeAsList() }
        verify(exactly = classList.size) { classQueries.update(any()) }
        coVerify { timetableNetworkClient.selectClassesByTimetableId(any()) }
        coVerify { appInfoGateway.checkAvailabilityOfClasses(any()) }
        coVerify { appInfoGateway.checkClassDependencies(classList) }
        confirmVerified(appInfoGateway, timetableNetworkClient, classQueries)
    }

    @Test fun checkAvailabilityOfClassesForNotEmptyList() = runTest {
        every {
            classQueries
                .selectByTimetableId(any())
                .executeAsList()
        } returns classList.map { ClassDtoMapper.toDbDto(it, false) }
        coEvery { appInfoGateway.checkClassDependencies(classList) } returns Either.right(Unit)

        appInfoGateway
            .checkAvailabilityOfClasses(timetable)
            .mapLeft { throw IllegalStateException() }

        coVerify { appInfoGateway.checkAvailabilityOfClasses(any()) }
        coVerify { appInfoGateway.checkClassDependencies(classList) }
        confirmVerified(appInfoGateway)
    }

    @Test
    fun checkAvailabilityOfClassesFail1() = runTest {
        every { classQueries.selectByTimetableId(any()).executeAsList() } returns emptyList()
        every { classQueries.update(any()) } returns Unit
        coEvery { timetableNetworkClient.selectClassesByTimetableId(any()) } returns Either.right(
            classList
        )
        coEvery {
            appInfoGateway.checkClassDependencies(any())
        } returns Either.left(DataFailure.ConnectionToRepository(""))

        appInfoGateway
            .checkAvailabilityOfClasses(timetable)
            .map { throw IllegalStateException() }

        verify { classQueries.selectByTimetableId(any()).executeAsList() }
        verify(exactly = classList.size) { classQueries.update(any()) }
        coVerify { timetableNetworkClient.selectClassesByTimetableId(any()) }
        coVerify { appInfoGateway.checkAvailabilityOfClasses(any()) }
        coVerify { appInfoGateway.checkClassDependencies(classList) }
        confirmVerified(appInfoGateway, timetableNetworkClient, classQueries)
    }

    @Test
    fun checkAvailabilityOfClassesFail2() = runTest {
        every {
            classQueries
                .selectByTimetableId(any())
                .executeAsList()
        } returns classList.map { ClassDtoMapper.toDbDto(it, false) }
        coEvery {
            appInfoGateway.checkClassDependencies(classList)
        } returns Either.left(DataFailure.ConnectionToRepository(""))

        appInfoGateway
            .checkAvailabilityOfClasses(timetable)
            .map { throw IllegalStateException() }

        coVerify { appInfoGateway.checkAvailabilityOfClasses(any()) }
        coVerify { appInfoGateway.checkClassDependencies(classList) }
        confirmVerified(appInfoGateway)
    }

    @Test fun checkClassDependenciesForEmptyList() = runTest {
        coEvery { appInfoGateway.checkAvailabilityOfClassTime(any()) } returns Either.right(Unit)
        coEvery { appInfoGateway.checkAvailabilityOfLecturer(any()) } returns Either.right(Unit)

        appInfoGateway
            .checkClassDependencies(emptyList())
            .mapLeft { throw IllegalStateException() }

        coVerify(inverse = true) { appInfoGateway.checkAvailabilityOfClassTime(any()) }
        coVerify(inverse = true) { appInfoGateway.checkAvailabilityOfLecturer(any()) }
        coVerify { appInfoGateway.checkClassDependencies(any()) }
        confirmVerified(appInfoGateway)
    }

    @Test fun checkClassDependenciesForNotEmptyList() = runTest {
        coEvery { appInfoGateway.checkAvailabilityOfClassTime(any()) } returns Either.right(Unit)
        coEvery { appInfoGateway.checkAvailabilityOfLecturer(any()) } returns Either.right(Unit)

        val list = List<ClassNetworkDto>(10) {
            mockk {
                every { classTimeId } returns it
                every { lecturerId } returns it
            }
        }

        appInfoGateway
            .checkClassDependencies(list)
            .mapLeft { throw IllegalStateException() }

        coVerify { appInfoGateway.checkAvailabilityOfClassTime(any()) }
        coVerify { appInfoGateway.checkAvailabilityOfLecturer(any()) }
        coVerify { appInfoGateway.checkClassDependencies(any()) }
        confirmVerified(appInfoGateway)
    }

    @Test fun checkClassDependenciesFailure1() = runTest {
        coEvery {
            appInfoGateway.checkAvailabilityOfClassTime(any())
        } returns Either.left(DataFailure.ConnectionToRepository(""))
        coEvery { appInfoGateway.checkAvailabilityOfLecturer(any()) } returns Either.right(Unit)

        val list = List<ClassNetworkDto>(10) {
            mockk {
                every { classTimeId } returns it
                every { lecturerId } returns it
            }
        }

        appInfoGateway
            .checkClassDependencies(list)
            .map { throw IllegalStateException() }

        coVerify { appInfoGateway.checkAvailabilityOfClassTime(any()) }
        coVerify(inverse = true) { appInfoGateway.checkAvailabilityOfLecturer(any()) }
        coVerify { appInfoGateway.checkClassDependencies(any()) }
        confirmVerified(appInfoGateway)
    }

    @Test fun checkClassDependenciesFailure2() = runTest {
        coEvery { appInfoGateway.checkAvailabilityOfClassTime(any()) } returns Either.right(Unit)
        coEvery {
            appInfoGateway.checkAvailabilityOfLecturer(any())
        } returns Either.left(DataFailure.ConnectionToRepository(""))

        val list = List<ClassNetworkDto>(10) {
            mockk {
                every { classTimeId } returns 0
                every { lecturerId } returns 0
            }
        }

        appInfoGateway
            .checkClassDependencies(list)
            .map { throw IllegalStateException() }

        coVerify { appInfoGateway.checkAvailabilityOfClassTime(any()) }
        coVerify { appInfoGateway.checkAvailabilityOfLecturer(any()) }
        coVerify { appInfoGateway.checkClassDependencies(any()) }
        confirmVerified(appInfoGateway)
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
        verify { classTimeQueries.update(any()) }
        coVerify { timetableNetworkClient.selectClassTimeById(classTime.id) }
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
        verify(inverse = true) { classTimeQueries.update(any()) }
        coVerify { timetableNetworkClient.selectClassTimeById(classTime.id) }
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
        verify { lecturerQueries.update(any()) }
        coVerify { timetableNetworkClient.selectLecturerById(lecturer.id) }
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
        verify(inverse = true) { lecturerQueries.update(any()) }
        coVerify { timetableNetworkClient.selectLecturerById(lecturer.id) }
        confirmVerified(lecturerQueries, timetableNetworkClient)
    }

    @Test
    fun clearUserInfo() = runTest {
        val userId = 0
        every { subscriptionQueries.selectByUserId(userId).executeAsList() } returns listOf(
            mockk { every { id } returns 0 },
            mockk { every { id } returns 1 },
            mockk { every { id } returns 2 }
        )
        every { subscriptionQueries.deleteById(any()) } returns Unit
        every { appInfoGateway.removeAllDependencies(any()) } returns Unit

        appInfoGateway.clearUserInfo(userId)

        verify { subscriptionQueries.selectByUserId(userId).executeAsList() }
        verify { subscriptionQueries.deleteById(any()) }
        verify { appInfoGateway.removeAllDependencies(any()) }
        coVerify { appInfoGateway.clearUserInfo(userId) }
        confirmVerified(subscriptionQueries, appInfoGateway)
    }
}