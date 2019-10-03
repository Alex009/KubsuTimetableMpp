package com.kubsu.timetable.data.network.sender

import com.kubsu.timetable.Either
import io.ktor.client.HttpClient

interface NetworkSender {
    val apiVersion: String
    val baseUrl: String

    suspend fun <L, R> handle(block: suspend HttpClient.() -> R): Either<L, R>
}