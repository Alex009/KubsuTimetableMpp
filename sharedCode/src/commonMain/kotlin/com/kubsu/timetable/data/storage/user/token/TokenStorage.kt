package com.kubsu.timetable.data.storage.user.token

interface TokenStorage {
    suspend fun set(token: TokenDto?)
    suspend fun get(): TokenDto?
}