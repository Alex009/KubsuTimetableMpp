package com.kubsu.timetable.platform

import com.squareup.sqldelight.db.SqlDriver

expect fun createDriver(args: PlatformArgs): SqlDriver
