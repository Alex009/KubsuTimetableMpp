package com.kubsu.timetable.data.mapper

import com.kubsu.timetable.data.network.dto.MainInfoNetworkDto
import com.kubsu.timetable.data.storage.maininfo.MainInfoStorageDto
import com.kubsu.timetable.domain.entity.MainInfoEntity

object MainInfoMapper {
    fun toEntity(storageDto: MainInfoStorageDto): MainInfoEntity =
        MainInfoEntity(storageDto.isNumerator)

    fun toEntity(networkDto: MainInfoNetworkDto): MainInfoEntity =
        MainInfoEntity(networkDto.isNumerator)

    fun toStorageDto(entity: MainInfoEntity): MainInfoStorageDto =
        MainInfoStorageDto(entity.isNumerator)

    fun toStorageDto(networkDto: MainInfoNetworkDto): MainInfoStorageDto =
        MainInfoStorageDto(networkDto.isNumerator)

    fun toNetworkDto(entity: MainInfoEntity): MainInfoNetworkDto =
        MainInfoNetworkDto(entity.isNumerator)

    fun toNetworkDto(storageDto: MainInfoStorageDto): MainInfoNetworkDto =
        MainInfoNetworkDto(storageDto.isNumerator)
}