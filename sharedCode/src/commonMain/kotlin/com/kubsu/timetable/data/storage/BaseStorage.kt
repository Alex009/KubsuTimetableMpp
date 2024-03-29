package com.kubsu.timetable.data.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

abstract class BaseStorage(private val settings: Settings) {
    fun getInt(key: String): Int? =
        settings[key, defaultInt].takeIf { it != defaultInt }

    fun getLong(key: String): Long? =
        settings[key, defaultLong].takeIf { it != defaultLong }

    fun getString(key: String): String? =
        settings[key, defaultStr].takeIf { it != defaultStr }

    fun getFloat(key: String): Float? =
        settings[key, defaultFloat].takeIf { it != defaultFloat }

    fun getDouble(key: String): Double? =
        settings[key, defaultDouble].takeIf { it != defaultDouble }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        settings[key, defaultValue]

    fun set(key: String, value: Int?): Unit =
        value?.let { settings[key] = it } ?: settings.remove(key)

    fun set(key: String, value: Long?): Unit =
        value?.let { settings[key] = it } ?: settings.remove(key)

    fun set(key: String, value: String?): Unit =
        value?.let { settings[key] = it } ?: settings.remove(key)

    fun set(key: String, value: Float?): Unit =
        value?.let { settings[key] = it } ?: settings.remove(key)

    fun set(key: String, value: Double?): Unit =
        value?.let { settings[key] = it } ?: settings.remove(key)

    fun set(key: String, value: Boolean?): Unit =
        value?.let { settings[key] = it } ?: settings.remove(key)

    private companion object {
        private const val defaultInt = -1
        private const val defaultLong = -1L
        private const val defaultFloat = -1.0F
        private const val defaultDouble = -1.0
        private const val defaultStr = "def_str"
    }
}