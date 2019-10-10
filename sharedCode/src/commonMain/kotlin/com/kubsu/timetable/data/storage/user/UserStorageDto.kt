package com.kubsu.timetable.data.storage.user

data class UserStorageDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val sessionKey: String,
    val email: String,
    val timestamp: Long
)