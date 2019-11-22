package com.kubsu.timetable.extensions

import com.egroden.teaco.Either
import com.egroden.teaco.fold
import com.egroden.teaco.left
import com.egroden.teaco.right
import com.kubsu.timetable.DataFailure
import kotlin.test.Test
import kotlin.test.assertEquals

class OtherTests {
    @Test
    fun emptyListToEitherList() {
        emptyList<Either<DataFailure, Unit>>()
            .toEitherList()
            .fold(
                ifLeft = {
                    throw IllegalStateException()
                },
                ifRight = {
                    assertEquals(it, emptyList())
                }
            )
    }

    @Test
    fun rightListToEitherList() {
        listOf(Either.right(Unit))
            .toEitherList()
            .fold(
                ifLeft = {
                    throw IllegalStateException()
                },
                ifRight = {
                    assertEquals(it, listOf(Unit))
                }
            )
    }

    @Test
    fun leftListToEitherList() {
        val failure = DataFailure.ConnectionToRepository()
        listOf(Either.left(failure))
            .toEitherList()
            .fold(
                ifLeft = {
                    assertEquals(it, failure)
                },
                ifRight = {
                    throw IllegalStateException()
                }
            )
    }

    @Test
    fun mixedListToEitherList() {
        val failure = DataFailure.ConnectionToRepository()
        listOf(
            Either.right(Unit),
            Either.left(failure)
        )
            .toEitherList()
            .fold(
                ifLeft = {
                    assertEquals(it, failure)
                },
                ifRight = {
                    throw IllegalStateException()
                }
            )
    }

    @Test
    fun indexOfFirstNotNull() {
        val list = listOf(1, 2, 3)
        assertEquals(list.indexOfFirstOrNull { it % 2 != 0 }, 0)
    }

    @Test
    fun indexOfFirstNull() {
        val list = listOf(2, 4, 6)
        assertEquals(list.indexOfFirstOrNull { it % 2 != 0 }, null)
    }
}