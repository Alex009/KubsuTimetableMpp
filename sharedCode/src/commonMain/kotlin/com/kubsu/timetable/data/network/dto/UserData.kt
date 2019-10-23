package com.kubsu.timetable.data.network.dto

import com.kubsu.timetable.data.storage.user.session.SessionDto

data class UserData(
    val user: UserNetworkDto,
    val session: SessionDto
)