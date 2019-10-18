package com.kubsu.timetable.extensions

import org.kodein.di.DKodeinAware
import org.kodein.di.Kodein
import org.kodein.di.erased
import kotlin.reflect.typeOf

@UseExperimental(ExperimentalStdlibApi::class)
inline fun <reified T : Any> nameWithGenerics(): String =
    typeOf<T>()
        .toString()
        .takeWhile { it != '(' }

inline fun <reified T : Any> Kodein.Builder.bindGeneric(): Kodein.Builder.TypeBinder<T> =
    Bind(
        erased(),
        nameWithGenerics<T>(),
        null
    )

inline fun <reified T : Any> DKodeinAware.instanceGeneric(): T =
    dkodein.Instance(erased(), nameWithGenerics<T>())