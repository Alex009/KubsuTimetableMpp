package com.kubsu.timetable.data.network.dto.data

class ClassNetworkDto(
    val id: Int,
    val title: String,
    val typeOfClass: Int,
    val classroom: String,
    val classTimeId: Int,
    val weekday: Int,
    val lecturerId: Int
)