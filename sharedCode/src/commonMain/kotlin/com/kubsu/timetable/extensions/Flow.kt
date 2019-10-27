package com.kubsu.timetable.extensions

import com.squareup.sqldelight.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

@UseExperimental(ExperimentalCoroutinesApi::class)
inline fun <T : Any, R> Query<T>.getContentFlow(
    crossinline getContent: suspend (Query<T>) -> R
): Flow<R> =
    this
        .asCallbackFlow()
        .map(getContent)
        .distinctUntilChanged()

@UseExperimental(ExperimentalCoroutinesApi::class)
fun <T : Any> Query<T>.asCallbackFlow(): Flow<Query<T>> =
    callbackFlow {
        offer(this@asCallbackFlow)
        val listener = object : Query.Listener {
            override fun queryResultsChanged() {
                offer(this@asCallbackFlow)
            }
        }

        addListener(listener)
        awaitClose { removeListener(listener) }
    }.buffer(Channel.CONFLATED)