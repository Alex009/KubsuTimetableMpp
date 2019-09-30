package com.kubsu.timetable.data.storage.user

data class UserStorageDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val timestamp: Long
)