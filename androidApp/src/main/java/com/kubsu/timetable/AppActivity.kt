package com.kubsu.timetable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionInteractor
import com.kubsu.timetable.domain.interactor.userInfo.UserInteractor

class AppActivity : AppCompatActivity() {
    lateinit var interactor: AuthInteractor
    lateinit var userInteractor: UserInteractor
    lateinit var subscriptionInteractor: SubscriptionInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun handleNetworkFail(fail: DataFailure) =
        when (fail) {
            is DataFailure.UnknownResponse ->
                println("Unknown fail: code = ${fail.code}, body = ${fail.body}, message = ${fail.debugMessage}")

            is DataFailure.NotAuthenticated ->
                println("NotAuthenticated")

            is DataFailure.ConnectionToRepository ->
                println("Connection fail: message = ${fail.debugMessage}")
        }
}
