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

        //val email = "indrih32141@gmail.com"
        //val password = "Margalo_pidor_69"
        GlobalScope.launch {
            /*interactor
                .signIn(email, password)
                .fold(
                    ifLeft = { requestFail ->
                        requestFail.handle(
                            ifDomain = { list ->
                                for (elem in list)
                                    Log.e(tag, "Domain failure: $elem")
                            },
                            ifData = { list ->
                                list.forEach(::handleNetworkFail)
                            }
                        )
                    },
                    ifRight = {
                        Log.i(tag, "Done: $it")
                    }
                )*/

            /*subscriptionInteractor
                .create(
                    subgroupId = 3,
                    subscriptionName = "Name",
                    isMain = true
                )
                .mapLeft { requestFail ->
                    requestFail.handle(
                        ifDomain = { list ->
                            for (elem in list)
                                Log.e(tag, "Domain failure: $elem")
                        },
                        ifData = { list ->
                            list.forEach(::handleNetworkFail)
                        }
                    )
                }

            subscriptionInteractor
                .getAll()
                .fold(
                    ifLeft = ::handleNetworkFail,
                    ifRight = {
                        println(it)
                        val subscription = it.firstOrNull()
                        if (subscription != null) {
                            subscriptionInteractor
                                .deleteById(subscription.id)
                                .mapLeft(::handleNetworkFail)
                        }
                    }
                )*/

            /*userInteractor
                .update(
                    userInteractor
                        .getCurrentUserOrThrow()
                        .copy(firstName = "sdf", lastName = "012")
                )
                .fold(
                    ifLeft = { requestFail ->
                        requestFail.handle(
                            ifDomain = { list ->
                                for (elem in list)
                                    Log.e(tag, "Domain failure: $elem")
                            },
                            ifData = { list ->
                                list.forEach(::handleNetworkFail)
                            }
                        )
                    },
                    ifRight = {
                        println("Done update")
                    }
                )

            interactor
                .logout()
                .fold(
                    ifLeft = ::handleNetworkFail,
                    ifRight = { println("Done logout") }
                )*/
        }
    }

    private fun handleNetworkFail(fail: DataFailure) {
        when (fail) {
            is DataFailure.UnknownResponse ->
                Log.e(
                    tag,
                    "Data unknown fail: code = ${fail.code}, body = ${fail.body}, message = ${fail.debugMessage}"
                )

            is DataFailure.NotAuthenticated ->
                Log.e(tag, "Data NotAuthenticated fail: ${fail.debugMessage}")

            is DataFailure.ConnectionToRepository ->
                Log.e(tag, "Data connection fail: message = ${fail.debugMessage}")
        }
    }
}
