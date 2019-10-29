package com.kubsu.timetable.extensions

import com.egroden.teaco.Either
import com.egroden.teaco.fold
import com.egroden.teaco.left
import com.egroden.teaco.right
import com.kubsu.timetable.domain.entity.Basename
import com.kubsu.timetable.domain.entity.diff.DataDiffEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun <T> T.checkWhenAllHandled() = Unit

fun <L, R> List<Either<L, R>>.toEitherList(): Either<L, List<R>> {
    val result = ArrayList<R>()
    for (either in this) {
        either.fold(
            ifLeft = { return Either.left(it) },
            ifRight = result::add
        )
    }
    return Either.right(result)
}

suspend inline fun <T> def(noinline block: suspend CoroutineScope.() -> T): T =
    withContext(Dispatchers.Default, block = block)

fun isMustDisplayInNotification(dataDiff: DataDiffEntity): Boolean =
    dataDiff.basename in listOf(Basename.Class, Basename.Lecturer)