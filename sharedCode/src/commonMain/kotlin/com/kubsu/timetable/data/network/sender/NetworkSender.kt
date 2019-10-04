package com.kubsu.timetable.data.network.sender

import com.kubsu.timetable.Either
import com.kubsu.timetable.data.network.sender.failure.ServerFailure
import io.ktor.client.HttpClient

interface NetworkSender {
    val apiVersion: String
    val baseUrl: String

    suspend fun <R> handle(block: suspend HttpClient.() -> R): Either<ServerFailure, R>
}