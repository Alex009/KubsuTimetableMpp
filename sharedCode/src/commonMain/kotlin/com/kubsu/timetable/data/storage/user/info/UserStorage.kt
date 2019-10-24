package com.kubsu.timetable.data.storage.user.info

interface UserStorage {
    fun set(user: UserDto?)
    fun get(): UserDto?
}