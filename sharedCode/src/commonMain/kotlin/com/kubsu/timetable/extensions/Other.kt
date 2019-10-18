package com.kubsu.timetable.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun <T> T.checkWhenAllHandled() = Unit

fun <T> List<T>.update(t: T, getId: (T) -> Int): List<T> {
    val result = ArrayList<T>()
    val id = getId(t)
    for (elem in this)
        result.add(
            if (id == getId(elem)) t else elem
        )
    return result
}

suspend inline fun <T> def(noinline block: suspend CoroutineScope.() -> T): T =
    withContext(Dispatchers.Default, block = block)
