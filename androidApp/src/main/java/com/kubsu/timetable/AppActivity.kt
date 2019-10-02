package com.kubsu.timetable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor

class AppActivity : AppCompatActivity() {
    lateinit var interactor: AuthInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*GlobalScope.launch {
            delay(3000)
            interactor.registrationUser("indrih17@gmail.com", "Kirl33434134")
        }*/
    }
}
