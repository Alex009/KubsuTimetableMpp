package com.kubsu.timetable.data.storage.user

import com.kubsu.timetable.data.storage.BaseStorage
import com.russhwolf.settings.Settings

class UserStorageImpl(
    settingsFactory: Settings.Factory
) : UserStorage,
    BaseStorage(settingsFactory.create("User")) {
    private val idPropName = "id"
    private val firstNamePropName = "first_name"
    private val secondNamePropName = "second_name"
    private val emailPropName = "email"
    private val timestampPropName = "timestamp"

    override suspend fun set(user: UserStorageDto?) {
        set(idPropName, user?.id)
        set(firstNamePropName, user?.firstName)
        set(secondNamePropName, user?.lastName)
        set(emailPropName, user?.email)
        set(timestampPropName, user?.timestamp)
    }

    override suspend fun get(): UserStorageDto? {
        val id: Int? = getInt(idPropName)
        val firstName: String? = getString(firstNamePropName)
        val secondName: String? = getString(secondNamePropName)
        val email: String? = getString(emailPropName)
        val timestamp: Long? = getLong(timestampPropName)

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