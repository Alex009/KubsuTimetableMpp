package com.kubsu.timetable.data.storage.user.info

import com.kubsu.timetable.data.storage.BaseStorage
import com.russhwolf.settings.Settings

class UserStorageImpl(
    settingsFactory: Settings.Factory
) : UserStorage,
    BaseStorage(settingsFactory.create("user_kubsu_timetable")) {
    override suspend fun set(user: UserDto?) {
        set(idPropName, user?.id)
        set(firstNamePropName, user?.firstName)
        set(secondNamePropName, user?.lastName)
        set(emailPropName, user?.email)
    }

    override suspend fun get(): UserDto? {
        val id: Int? = getInt(idPropName)
        val firstName: String? = getString(firstNamePropName)
        val lastName: String? = getString(secondNamePropName)
        val email: String? = getString(emailPropName)
        return if (id != null
            && firstName != null
            && lastName != null
            && email != null
        )
            UserDto(
                id = id,
                firstName = firstName,
                lastName = lastName,
                email = email
            )
        else
            null
    }

    private companion object {
        private const val idPropName = "id_prop"
        private const val firstNamePropName = "first_name_prop"
        private const val secondNamePropName = "second_name_prop"
        private const val emailPropName = "email_prop"
    }
}