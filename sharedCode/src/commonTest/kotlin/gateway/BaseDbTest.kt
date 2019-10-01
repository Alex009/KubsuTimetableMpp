package gateway

import com.kubsu.timetable.data.db.MyDatabase
import com.squareup.sqldelight.db.SqlDriver
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

open class BaseDbTest {
    private var driver: SqlDriver? = null
    private var myDatabase: MyDatabase? = null

    @BeforeTest
    fun initDb() {

    }

    @AfterTest
    fun closeDb() {
        driver?.close()
        driver = null
        myDatabase = null
    }
}
