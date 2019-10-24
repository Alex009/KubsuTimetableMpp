package com.kubsu.timetable.data.storage.user.session

import com.egroden.teaco.Either
import com.egroden.teaco.left
import com.egroden.teaco.right
import com.kubsu.timetable.DataFailure

interface SessionStorage {
    fun set(session: SessionDto?)
    fun get(): SessionDto?
}

fun SessionStorage.getEitherFailure(): Either<DataFailure, SessionDto> =
    get()
        ?.let(Either.Companion::right)
        ?: Either.left(DataFailure.NotAuthenticated())