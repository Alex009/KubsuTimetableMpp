package com.kubsu.timetable.data.storage.maininfo

import com.github.florent37.preferences.Preferences

class MainInfoStorageImpl : MainInfoStorage {
    private val isNumeratorPropName = "is_numerator"
    private val pref = Preferences("MainInfo")

    override suspend fun set(mainInfo: MainInfoStorageDto) =
        pref.setBoolean(
            isNumeratorPropName,
            mainInfo.isNumerator
        )

    override suspend fun get(): MainInfoStorageDto? {
        val isNumerator: Boolean? = pref.getBoolean(isNumeratorPropName)
        return if (isNumerator != null) MainInfoStorageDto(isNumerator) else null
    }
}