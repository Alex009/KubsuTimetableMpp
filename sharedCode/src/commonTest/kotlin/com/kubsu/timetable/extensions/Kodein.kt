package com.kubsu.timetable.extensions

import kotlin.test.Test
import kotlin.test.assertEquals

class KodeinTests {
    class Tested

    @Test
    fun testGenericName() {
        assertEquals(nameWithGenerics<Int>(), "kotlin.Int")
        assertEquals(nameWithGenerics<String>(), "kotlin.String")
        assertEquals(
            nameWithGenerics<Tested>(),
            "com.kubsu.timetable.extensions.KodeinTests.Tested"
        )
    }
}