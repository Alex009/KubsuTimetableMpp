package com.kubsu.timetable.data.mapper

import com.kubsu.timetable.data.network.dto.UserNetworkDto
import com.kubsu.timetable.data.storage.user.UserStorageDto
import com.kubsu.timetable.domain.entity.Timestamp
import com.kubsu.timetable.domain.entity.UserEntity

object UserDtoMapper {
    fun toEntity(storageDto: UserStorageDto): UserEntity =
        UserEntity(
            id = storageDto.id,
            firstName = storageDto.firstName,
            lastName = storageDto.lastName,
            email = storageDto.email,
            timestamp = Timestamp(storageDto.timestamp),
            sessionKey = storageDto.sessionKey
        )

    fun toEntity(networkDto: UserNetworkDto, timestamp: Timestamp): UserEntity =
        UserEntity(
            id = networkDto.id,
            firstName = networkDto.firstName,
            lastName = networkDto.lastName,
            email = networkDto.email,
            timestamp = timestamp,
            sessionKey = networkDto.sessionKey
        )

    fun toStorageDto(entity: UserEntity): UserStorageDto =
        UserStorageDto(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            email = entity.email,
            timestamp = entity.timestamp.value,
            sessionKey = entity.sessionKey
        )

    fun toStorageDto(entity: UserNetworkDto, timestamp: Timestamp): UserStorageDto =
        UserStorageDto(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            email = entity.email,
            timestamp = timestamp.value,
            sessionKey = entity.sessionKey
        )

    fun toNetworkDto(entity: UserEntity): UserNetworkDto =
        UserNetworkDto(
            id = entity.id,
            email = entity.email,
            firstName = entity.firstName,
            lastName = entity.lastName,
            sessionKey = entity.sessionKey
        )

    fun toNetworkDto(storageDto: UserStorageDto): UserNetworkDto =
        UserNetworkDto(
            id = storageDto.id,
            email = storageDto.email,
            firstName = storageDto.firstName,
            lastName = storageDto.lastName,
            sessionKey = storageDto.sessionKey
        )
}