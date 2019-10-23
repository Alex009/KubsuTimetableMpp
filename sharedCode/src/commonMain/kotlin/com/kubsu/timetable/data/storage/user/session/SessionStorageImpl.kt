package com.kubsu.timetable.data.storage.user.session

import com.kubsu.timetable.data.storage.BaseStorage
import com.kubsu.timetable.domain.entity.Timestamp
import com.russhwolf.settings.Settings

class SessionStorageImpl(
    settingsFactory: Settings.Factory
) : SessionStorage,
    BaseStorage(settingsFactory.create("timetable_user_session")) {
    override suspend fun set(session: SessionDto?) {
        set(timestampPropName, session?.timestamp?.value)
        set(sessionIdPropName, session?.id)
    }

    override suspend fun get(): SessionDto? {
        val timestamp = getLong(timestampPropName)
        val sessionId = getString(sessionIdPropName)
        return if (timestamp != null && sessionId != null)
            SessionDto(id = sessionId, timestamp = Timestamp(timestamp))
        else
            null
    }

    companion object {
        const val timestampPropName = "timestamp"
        const val sessionIdPropName = "session_id"
    }
}