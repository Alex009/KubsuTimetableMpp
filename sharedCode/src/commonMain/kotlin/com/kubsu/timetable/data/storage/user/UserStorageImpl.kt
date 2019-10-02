package com.kubsu.timetable.data.storage.user

import com.github.florent37.preferences.Preferences

class UserStorageImpl : UserStorage {
    private val idPropName = "id"
    private val firstNamePropName = "first_name"
    private val secondNamePropName = "second_name"
    private val emailPropName = "email"
    private val timestampPropName = "timestamp"

    private val defaultInt = -1
    private val defaultLong = -1L

    private val pref = Preferences("User")

    override suspend fun set(user: UserStorageDto?) {
        pref.run {
            setInt(idPropName, user?.id ?: defaultInt)
            setString(firstNamePropName, user?.firstName)
            setString(secondNamePropName, user?.lastName)
            setString(emailPropName, user?.email)
            setLong(timestampPropName, user?.timestamp ?: defaultLong)
        }
    }

    override suspend fun get(): UserStorageDto? {
        val id: Int?
        val firstName: String?
        val secondName: String?
        val email: String?
        val timestamp: Long?
        getPrivatePreferences().let {
            id = it.getInt(idPropName, defaultInt).takeIf { id -> id != defaultInt }
            firstName = it.getString(firstNamePropName, null)
            secondName = it.getString(secondNamePropName, null)
            email = it.getString(emailPropName, null)
            timestamp = it
                .getLong(timestampPropName, defaultLong)
                .takeIf { timestamp -> timestamp != defaultLong }
        }
        return if (id != null
            && firstName != null
            && secondName != null
            && email != null
            && timestamp != null
        )
            UserStorageDto(
                id = id,
                firstName = firstName,
                lastName = secondName,
                email = email,
                timestamp = timestamp
            )
        else
            null
    }
}