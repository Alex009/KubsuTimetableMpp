package com.kubsu.timetable.platform

import com.kubsu.timetable.data.db.MyDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.ios.NativeSqliteDriver

actual fun createDriver(args: PlatformArgs): SqlDriver =
    NativeSqliteDriver(MyDatabase.Schema, "timetable.db")