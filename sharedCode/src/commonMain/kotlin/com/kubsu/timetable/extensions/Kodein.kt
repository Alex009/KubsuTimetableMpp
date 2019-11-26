package com.kubsu.timetable.extensions

import org.kodein.di.*
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

inline fun <reified A, reified T : Any> DKodeinAware.instanceGeneric(arg: A): T =
    dkodein.Instance(erased(), erased(), nameWithGenerics<T>(), arg)

inline fun <reified T : Any> KodeinAware.instanceDep(): T =
    direct.Instance(erased(), null)

inline fun <reified A, reified T : Any> KodeinAware.instanceDep(arg: A): T =
    direct.Instance(erased(), erased(), nameWithGenerics<T>(), arg)