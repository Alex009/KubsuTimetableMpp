package com.kubsu.timetable.data.storage.user

interface UserStorage {
    suspend fun set(user: UserStorageDto?)
    suspend fun get(): UserStorageDto?
}