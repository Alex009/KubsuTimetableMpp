package platform

import com.kubsu.timetable.data.db.MyDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual fun createDriver(args: PlatformArgs): SqlDriver =
    AndroidSqliteDriver(MyDatabase.Schema, args.context, "timetable.db")
