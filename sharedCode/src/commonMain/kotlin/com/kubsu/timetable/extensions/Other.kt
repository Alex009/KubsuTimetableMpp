package com.kubsu.timetable.extensions

import com.soywiz.klock.DateTime
import com.soywiz.klock.DayOfWeek
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun <T> T.checkWhenAllHandled() = Unit

fun getCurrentDayOfWeek(): DayOfWeek =
    DateTime.nowLocal().dayOfWeek

fun <T> List<T>.update(t: T, getId: (T) -> Int): List<T> {
    val result = ArrayList<T>()
    val id = getId(t)
    for (elem in this)
        result.add(
            if (id != getId(elem)) elem else t
        )
    return result
}

fun <T> List<T>.delete(t: T, getId: (T) -> Int): List<T> {
    val result = ArrayList<T>()
    val id = getId(t)
    for (elem in this)
        if (id != getId(elem))
            result.add(elem)
    return result
}

suspend inline fun <T> def(noinline block: suspend CoroutineScope.() -> T): T =
    withContext(Dispatchers.Default, block = block)
