package com.kubsu.timetable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kubsu.timetable.domain.interactor.sync.SyncMixinInteractor

class AppActivity : AppCompatActivity() {
    lateinit var interactor: SyncMixinInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
