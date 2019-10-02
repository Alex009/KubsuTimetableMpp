package com.kubsu.timetable.domain.entity

import com.soywiz.klock.DateTime

inline class Timestamp(val value: Long) {
    companion object {
        fun create() = Timestamp(DateTime.nowUnixLong() / 1000)
    }
}