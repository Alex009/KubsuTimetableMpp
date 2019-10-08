package com.kubsu.timetable.data.network.sender.failure

import com.kubsu.timetable.DataFailure

sealed class ServerFailure {
    class Response(
        val message: String?,
        val body: String,
        val code: Int
    ) : ServerFailure()

    class Connection(val message: String?) : ServerFailure()
}

fun toNetworkFail(failure: ServerFailure): DataFailure =
    when (failure) {
        is ServerFailure.Response ->
            DataFailure.UnknownResponse(failure.code, failure.body, failure.message)

        is ServerFailure.Connection ->
            DataFailure.ConnectionToRepository(failure.message)
    }