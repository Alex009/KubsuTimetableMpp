package com.kubsu.timetable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import com.kubsu.timetable.domain.interactor.main.UserInteractor
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionInteractor

class AppActivity : AppCompatActivity() {
    lateinit var interactor: AuthInteractor
    lateinit var userInteractor: UserInteractor
    lateinit var subscriptionInteractor: SubscriptionInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println(isNumerator())
    }

    private fun handleNetworkFail(fail: NetworkFailure) =
        when (fail) {
            is NetworkFailure.UnknownFailure ->
                println("Unknown fail: code = ${fail.code}, body = ${fail.body}, message = ${fail.debugMessage}")

            is NetworkFailure.Connection ->
                println("Connection fail: message = ${fail.debugMessage}")
        }
}
