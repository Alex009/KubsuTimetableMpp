package com.kubsu.timetable.domain.entity

data class UserEntity(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val sessionKey: String,
    val email: String,
    val timestamp: Timestamp
)