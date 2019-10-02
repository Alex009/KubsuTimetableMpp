package com.kubsu.timetable.data.storage.maininfo

import com.kubsu.timetable.data.storage.BaseStorage
import com.russhwolf.settings.Settings

class MainInfoStorageImpl(
    settingsFactory: Settings.Factory
) : MainInfoStorage,
    BaseStorage(settingsFactory.create("main_info")) {
    private val isNumeratorPropName = "is_numerator"

    override suspend fun set(mainInfo: MainInfoStorageDto?) =
        set(isNumeratorPropName, mainInfo?.isNumerator)

    override suspend fun get(): MainInfoStorageDto? {
        val isNumerator: Boolean = getBoolean(isNumeratorPropName, false)
        return MainInfoStorageDto(isNumerator)
    }
}