package com.kubsu.timetable.extensions

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

@UseExperimental(ExperimentalCoroutinesApi::class)
inline fun <T : Any, R> Query<T>.asFilteredFlow(
    crossinline getRowType: suspend (Query<T>) -> R
): Flow<R> =
    this
        .asFlow()
        .map(getRowType)
        .distinctUntilChanged()

@UseExperimental(ExperimentalCoroutinesApi::class)
inline fun <T : Any, R : Any> Query<T>.asFilteredFlowNotNull(
    crossinline getRowType: suspend (Query<T>) -> R?
): Flow<R> =
    this
        .asFlow()
        .mapNotNull(getRowType)
        .distinctUntilChanged()