package com.kubsu.timetable.data.storage.user.info

interface UserStorage {
    suspend fun set(user: UserDto?)
    suspend fun get(): UserDto?
}