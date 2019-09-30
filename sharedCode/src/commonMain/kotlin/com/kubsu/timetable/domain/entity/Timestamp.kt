package com.kubsu.timetable.domain.entity

inline class Timestamp(val value: Long)

expect fun createTimestamp(): Timestamp