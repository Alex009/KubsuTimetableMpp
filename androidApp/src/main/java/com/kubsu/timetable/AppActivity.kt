package com.kubsu.timetable

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionInteractor
import com.kubsu.timetable.domain.interactor.userInfo.UserInteractor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AppActivity : AppCompatActivity() {
    lateinit var interactor: AuthInteractor
    lateinit var userInteractor: UserInteractor
    lateinit var subscriptionInteractor: SubscriptionInteractor
    private val tag = "AppActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val email = "indrih17@gmail.com"
        val password = "1020321"
        GlobalScope.launch {
            userInteractor
                .registrationUser(email, password)
                .fold(
                    ifLeft = { requestFail ->
                        requestFail.handle(
                            ifDomain = {
                                Log.e(tag, "Domain failure: $it")
                            },
                            ifData = { list ->
                                list.forEach(::handleNetworkFail)
                            }
                        )
                    },
                    ifRight = {
                        Log.i(tag, "Done")
                    }
                )
        }
    }

    private fun handleNetworkFail(fail: DataFailure) {
        val res = when (fail) {
            is DataFailure.UnknownResponse ->
                Log.e(
                    tag,
                    "Unknown fail: code = ${fail.code}, body = ${fail.body}, message = ${fail.debugMessage}"
                )

            is DataFailure.NotAuthenticated ->
                Log.e(tag, "NotAuthenticated")

            is DataFailure.ConnectionToRepository ->
                Log.e(tag, "Connection fail: message = ${fail.debugMessage}")
        }
    }
}
