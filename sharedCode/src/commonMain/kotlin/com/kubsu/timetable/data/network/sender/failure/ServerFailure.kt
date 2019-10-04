package com.kubsu.timetable.data.network.sender.failure

import com.kubsu.timetable.NetworkFailure

sealed class ServerFailure {
    class Response(
        val message: String?,
        val body: String,
        val code: Int
    ) : ServerFailure()

    class Connection(val message: String?) : ServerFailure()
}

fun toNetworkFail(failure: ServerFailure): NetworkFailure =
    when (failure) {
        is ServerFailure.Response ->
            NetworkFailure.UnknownFailure(failure.message, failure.code, failure.body)

        is ServerFailure.Connection ->
            NetworkFailure.Connection(failure.message)
    }