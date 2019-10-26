package com.kubsu.timetable.data.storage.user.token

import com.kubsu.timetable.data.storage.BaseStorage
import com.russhwolf.settings.Settings

class TokenStorageImpl(
    settingsFactory: Settings.Factory
) : TokenStorage,
    BaseStorage(settingsFactory.create("current_token_kubsu_timetable")) {
    override fun set(token: Token?) {
        set(tokenPropName, token?.value)
        set(deliveredPropName, token is DeliveredToken)
    }

    override fun get(): Token? {
        val token = getString(tokenPropName)
        val delivered = getBoolean(deliveredPropName, false)
        return if (token != null)
            if (delivered)
                DeliveredToken(token)
            else
                UndeliveredToken(token)
        else
            null
    }

    companion object {
        const val tokenPropName = "token_prop"
        const val deliveredPropName = "delivered_prop"
    }
}