package com.kubsu.timetable.data.storage.user.token

sealed class Token(val value: String)

class DeliveredToken(value: String) : Token(value)

class UndeliveredToken(value: String) : Token(value)