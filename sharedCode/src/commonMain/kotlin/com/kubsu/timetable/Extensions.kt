package com.kubsu.timetable

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T> def(noinline block: suspend CoroutineScope.() -> T): T =
    withContext(Dispatchers.Default, block = block)