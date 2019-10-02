package platform

import com.kubsu.timetable.data.db.MyDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.ios.NativeSqliteDriver

actual class PlatformDriverArgs

actual fun createDriver(args: PlatformDriverArgs): SqlDriver =
    NativeSqliteDriver(MyDatabase.Schema, "timetable.db")