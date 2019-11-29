package com.kubsu.timetable.platform.di

    //typealias AliasRender<State> = (State) -> Unit

/*typealias AliasRender<State> = (State) -> Unit

fun <T> someFun(aliasRender: AliasRender<T>) {
}
*/

typealias AliasRender<State> = (State) -> Unit

class Kek<T : Any> {
    fun someFun(aliasRender: AliasRender<T>) {
    }
}