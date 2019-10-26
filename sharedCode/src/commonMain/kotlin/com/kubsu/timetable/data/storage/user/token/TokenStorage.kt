package com.kubsu.timetable.data.storage.user.token

interface TokenStorage {
    fun set(token: Token?)
    fun get(): Token?
}