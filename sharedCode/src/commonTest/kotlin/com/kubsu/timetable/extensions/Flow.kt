package com.kubsu.timetable.extensions

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FlowTests {
    @Test
    fun withOldValueTest() = runTest {
        val original = flowOf(1, 2, 3, 4).onEach { delay(100) }
        val result = original
            .withOldValue { old, new -> old + new }
            .toList()
        assertEquals(result, listOf(1, 3, 6, 10))
    }

    @Test
    fun filterPreviousTest1() = runTest {
        val flowOfLists = flowOf(
            listOf(1, 2, 3),
            listOf(1, 2, 3, 4)
        )
        val result = flowOfLists
            .filterPrevious()
            .toList()
        assertEquals(result, listOf(listOf(1, 2, 3), listOf(4)))
    }

    @Test
    fun filterPreviousTest2() = runTest {
        data class Testable(
            val foo: Int,
            val bar: String
        )

        val flowOfLists = flowOf(
            listOf(
                Testable(1, "1"),
                Testable(2, "2")
            ),
            listOf(
                Testable(1, "1"),
                Testable(2, "2"),
                Testable(3, "3")
            )
        )
        val result = flowOfLists
            .filterPrevious()
            .toList()
        println(result)
    }
}