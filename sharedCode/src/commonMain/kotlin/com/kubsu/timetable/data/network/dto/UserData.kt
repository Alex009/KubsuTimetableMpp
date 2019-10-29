package com.kubsu.timetable.data.network.dto

import com.kubsu.timetable.data.storage.user.session.Session

data class UserData(
    val user: UserNetworkDto,
    val session: Session
)