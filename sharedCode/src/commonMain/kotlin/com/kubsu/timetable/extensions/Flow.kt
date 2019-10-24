package com.kubsu.timetable.extensions

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

@UseExperimental(ExperimentalCoroutinesApi::class)
fun <T : Any, R> Query<T>.asFilteredFlow(getRowType: (Query<T>) -> R): Flow<R> =
    this
        .asFlow()
        .map { getRowType(it) }
        .distinctUntilChanged()

@UseExperimental(ExperimentalCoroutinesApi::class)
fun <T : Any, R : Any> Query<T>.asFilteredFlowNotNull(getRowType: (Query<T>) -> R?): Flow<R> =
    this
        .asFlow()
        .mapNotNull { getRowType(it) }
        .distinctUntilChanged()