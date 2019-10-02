package com.kubsu.timetable.data.storage.maininfo

interface MainInfoStorage {
    suspend fun set(mainInfo: MainInfoStorageDto)
    suspend fun get(): MainInfoStorageDto?
}