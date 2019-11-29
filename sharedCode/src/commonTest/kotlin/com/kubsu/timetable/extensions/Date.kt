package com.kubsu.timetable.extensions

import com.kubsu.timetable.presentation.timetable.model.TimetableInfoToDisplay
import com.soywiz.klock.DayOfWeek
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTests {
    @BeforeTest
    fun before() {
        mockkStatic("com.kubsu.timetable.extensions.DateKt")
        every { getCurrentDayOfWeek() } returns DayOfWeek.Friday
    }

    @Test
    fun testOfIndexSearchForCurrentDay() {
        val friday = listOf(
            TimetableInfoToDisplay.Day(DayOfWeek.Friday)
        )
        val fridayCasses = List<TimetableInfoToDisplay>(5) {
            TimetableInfoToDisplay.Class(
                mockk { every { id } returns it }
            )
        }
        val result = friday + fridayCasses
        assertEquals(result.indexOfNearestDayOrNull(), 0)
    }

    @Test
    fun testOfIndexSearchForNextWeek() {
        val monday = listOf(
            TimetableInfoToDisplay.Day(DayOfWeek.Monday)
        )
        val mondayClasses = List<TimetableInfoToDisplay>(5) {
            TimetableInfoToDisplay.Class(
                mockk { every { id } returns it }
            )
        }
        val result = monday + mondayClasses
        assertEquals(result.indexOfNearestDayOrNull(), 0)
    }

    @Test
    fun testOfIndexSearchForNormalWeek() {
        val thursday = listOf(
            TimetableInfoToDisplay.Day(DayOfWeek.Thursday)
        )
        val thursdayClasses = List<TimetableInfoToDisplay>(5) {
            TimetableInfoToDisplay.Class(
                mockk { every { id } returns it }
            )
        }
        val saturday = listOf(
            TimetableInfoToDisplay.Day(DayOfWeek.Saturday)
        )
        val saturdayClasses = List<TimetableInfoToDisplay>(5) {
            TimetableInfoToDisplay.Class(
                mockk { every { id } returns it }
            )
        }
        val result = thursday + thursdayClasses + saturday + saturdayClasses
        assertEquals(result.indexOfNearestDayOrNull(), (thursday + thursdayClasses).size)
    }

    @Test
    fun testOfIndexSearchForEmptyList() {
        assertEquals(
            emptyList<TimetableInfoToDisplay>().indexOfNearestDayOrNull(),
            null
        )
    }
}