package com.kubsu.timetable.data.mapper

import com.kubsu.timetable.data.network.dto.UserNetworkDto
import com.kubsu.timetable.data.storage.user.info.UserDto
import com.kubsu.timetable.domain.entity.UserEntity

object UserDtoMapper {
    fun toEntity(storageDto: UserDto): UserEntity =
        UserEntity(
            id = storageDto.id,
            firstName = storageDto.firstName,
            lastName = storageDto.lastName,
            email = storageDto.email
        )

    fun toEntity(networkDto: UserNetworkDto): UserEntity =
        UserEntity(
            id = networkDto.id,
            firstName = networkDto.firstName,
            lastName = networkDto.lastName,
            email = networkDto.email
        )

    fun toStorageDto(entity: UserEntity): UserDto =
        UserDto(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            email = entity.email
        )

    fun toStorageDto(entity: UserNetworkDto): UserDto =
        UserDto(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            email = entity.email
        )

    fun toNetworkDto(entity: UserEntity): UserNetworkDto =
        UserNetworkDto(
            id = entity.id,
            email = entity.email,
            firstName = entity.firstName,
            lastName = entity.lastName
        )

    fun toNetworkDto(storageDto: UserDto): UserNetworkDto =
        UserNetworkDto(
            id = storageDto.id,
            email = storageDto.email,
            firstName = storageDto.firstName,
            lastName = storageDto.lastName
        )
}