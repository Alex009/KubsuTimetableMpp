package com.kubsu.timetable.data.storage.user.session

import com.kubsu.timetable.data.storage.BaseStorage
import com.kubsu.timetable.domain.entity.Timestamp
import com.russhwolf.settings.Settings

class SessionStorageImpl(
    settingsFactory: Settings.Factory
) : SessionStorage,
    BaseStorage(settingsFactory.create("session_kubsu_timetable")) {
    override fun set(session: Session?) {
        set(timestampPropName, session?.timestamp?.value)
        set(sessionIdPropName, session?.id)
    }

    override fun get(): Session? {
        val timestamp = getLong(timestampPropName)
        val sessionId = getString(sessionIdPropName)
        return if (timestamp != null && sessionId != null)
            Session(
                id = sessionId,
                timestamp = Timestamp(timestamp)
            )
        else
            null
    }

    companion object {
        const val timestampPropName = "timestamp_prop"
        const val sessionIdPropName = "session_id_prop"
    }
}