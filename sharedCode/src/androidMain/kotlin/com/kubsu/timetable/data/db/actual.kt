package com.kubsu.timetable.data.db

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class PlatformDriverArgs(val context: Context)

actual fun createDriver(args: PlatformDriverArgs): SqlDriver =
    AndroidSqliteDriver(MyDatabase.Schema, args.context, "timetable.db")
