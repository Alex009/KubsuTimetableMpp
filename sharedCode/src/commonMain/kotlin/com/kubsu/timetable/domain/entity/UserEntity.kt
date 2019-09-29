package com.kubsu.timetable.domain.entity

data class UserEntity(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val timestamp: Timestamp
)