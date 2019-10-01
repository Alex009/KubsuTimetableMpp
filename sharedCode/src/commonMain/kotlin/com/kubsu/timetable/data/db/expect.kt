package com.kubsu.timetable.data.db

import com.squareup.sqldelight.db.SqlDriver

expect class PlatformDriverArgs

expect fun createDriver(args: PlatformDriverArgs): SqlDriver
