package com.kubsu.timetable.data.storage.user.token

interface TokenStorage {
    fun set(token: TokenDto?)
    fun get(): TokenDto?
}