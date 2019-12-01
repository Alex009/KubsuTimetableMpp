package com.kubsu.timetable.platform.di

typealias AliasRender<State> = (State) -> Unit

class Kek<T : Any> {
    fun someFun(aliasRender: AliasRender<T>) {
    }
}