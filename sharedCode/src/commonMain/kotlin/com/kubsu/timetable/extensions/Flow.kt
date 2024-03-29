package com.kubsu.timetable.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

fun <T : Any> Flow<T>.withOldValue(update: (old: T, new: T) -> T): Flow<T> = flow {
    var old: T? = null

    collect { new ->
        val result = old?.let { update(it, new) } ?: new
        old = result
        emit(result)
    }
}

/**
 * @see [filterPreviousBy].
 */
fun <T : Any> Flow<List<T>>.filterPrevious(): Flow<List<T>> = filterPreviousBy { it }

/**
 * @see [filterPreviousBy].
 */
fun <T : Any> Flow<List<T>>.filterPrevious(areEquivalent: (old: T, new: T) -> Boolean): Flow<List<T>> =
    filterPreviousBy(keySelector = { it }, areEquivalent = areEquivalent)

/**
 * @see [filterPreviousBy].
 */
fun <T : Any, K : Any> Flow<List<T>>.filterPreviousBy(keySelector: (T) -> K): Flow<List<T>> =
    filterPreviousBy(keySelector = keySelector, areEquivalent = { old, new -> old == new })

/**
 * Use with primitive types or data classes.
 */
inline fun <T : Any, K : Any> Flow<List<T>>.filterPreviousBy(
    noinline keySelector: (T) -> K,
    crossinline areEquivalent: (old: K, new: K) -> Boolean
): Flow<List<T>> = flow {
    var previousKeys: List<K>? = null
    collect { list ->
        val result = previousKeys
            ?.let { previous ->
                list.filter { elem ->
                    val currentKey = keySelector(elem)
                    previous.firstOrNull { areEquivalent(currentKey, it) } == null
                }
            }
            ?: list
        previousKeys = list.map(keySelector)
        emit(result)
    }
}
