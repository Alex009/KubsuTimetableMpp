package com.kubsu.timetable

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

suspend inline fun <T> def(noinline block: suspend CoroutineScope.() -> T): T =
    withContext(Dispatchers.Default, block = block)

fun <T> flowOfIterable(iterable: Iterable<T>): Flow<T> = flow {
    for (element in iterable)
        emit(element)
}

class StopException : Exception()

suspend inline fun <L, R> Flow<Either<L, R>>.collectRightListOrFirstLeft(): Either<L, List<R>> {
    val result = ArrayList<R>()
    var fail: L? = null
    try {
        collect {
            it.fold(
                ifRight = result::add,
                ifLeft = { left ->
                    fail = left
                    throw StopException()
                }
            )
        }
    } catch (e: StopException) {
        // Do nothing
    }
    return fail?.let { Either.left(it) } ?: Either.right(result)
}