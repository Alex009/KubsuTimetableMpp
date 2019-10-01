package com.kubsu.timetable.domain.interactor.main

import com.kubsu.timetable.Either
import com.kubsu.timetable.NetworkFailure
import com.kubsu.timetable.data.network.dto.MainInfoNetworkDto
import com.kubsu.timetable.domain.entity.MainInfoEntity
import com.kubsu.timetable.domain.entity.UserEntity

interface MainGateway {
    suspend fun getMainInfo(): Either<NetworkFailure, MainInfoEntity>
    suspend fun setMainInfoEntity(mainInfo: MainInfoEntity?)
    suspend fun setMainInfoNetworkDto(mainInfo: MainInfoNetworkDto?)

    suspend fun getCurrentUser(): UserEntity?
    suspend fun set(userEntity: UserEntity?)
}