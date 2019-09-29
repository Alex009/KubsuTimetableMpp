package com.kubsu.timetable

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T> def(noinline block: suspend CoroutineScope.() -> T): T =
    withContext(Dispatchers.Default, block = block)

inline fun <L, R> Either<L, R>.alsoIfRight(f: (R) -> Unit): Either<L, R> {
    if (this is Either.Right<R>) f(b)
    return this
}