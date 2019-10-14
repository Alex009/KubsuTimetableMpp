package com.kubsu.timetable.firebase

class NotAuthenticatedException(override val message: String?) : Exception()

class UnknownResponseException(
    val code: Int,
    val body: String,
    override val message: String?
) : Exception()