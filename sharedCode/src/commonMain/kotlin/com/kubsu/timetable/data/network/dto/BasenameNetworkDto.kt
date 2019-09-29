package com.kubsu.timetable.data.network.dto

sealed class BasenameNetworkDto(val name: String) {
    object Subscription : BasenameNetworkDto("subscriptions")
    object Timetable : BasenameNetworkDto("timetables")
    object Lecturer : BasenameNetworkDto("lecturers")
    object Class : BasenameNetworkDto("classes")
    object MainInfo : BasenameNetworkDto("main_info")
}