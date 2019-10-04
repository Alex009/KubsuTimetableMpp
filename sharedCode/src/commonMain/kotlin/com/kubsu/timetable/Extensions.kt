package com.kubsu.timetable

import com.soywiz.klock.DateTime
import com.soywiz.klock.weekOfYear1
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T> def(noinline block: suspend CoroutineScope.() -> T): T =
    withContext(Dispatchers.Default, block = block)

fun numberOfWeek(): Int =
    DateTime.now().weekOfYear1

fun isNumerator(): Boolean =
    numberOfWeek() and 1 == 0