package com.kubsu.timetable

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kubsu.timetable.domain.interactor.auth.AuthInteractor
import com.kubsu.timetable.domain.interactor.subscription.SubscriptionInteractor
import com.kubsu.timetable.domain.interactor.sync.SyncMixinInteractor
import com.kubsu.timetable.domain.interactor.timetable.TimetableInteractor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AppActivity : AppCompatActivity() {
    private val tag = "AppActivity"
    lateinit var syncMixinInteractor: SyncMixinInteractor
    lateinit var timetableInteractor: TimetableInteractor
    lateinit var authInteractor: AuthInteractor
    lateinit var subscriptionsInteractor: SubscriptionInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val email = "indrih32311@gmail.com"
        val password = "Margalo_pidor_69"
        GlobalScope.launch {
            authInteractor
                .logout()
                .fold(
                    ifLeft = ::handleDataFailure,
                    ifRight = {
                        Log.i(tag, "Success logout")
                    }
                )
            authInteractor
                .signIn(email, password)
                .fold(
                    ifLeft = ::handleRequestFailure,
                    ifRight = {
                        Log.i(tag, "User: $it")
                    }
                )

            /*subscriptionsInteractor
                .selectFacultyList()
                .fold(
                    ifLeft = ::handleDataFailure,
                    ifRight = { list ->
                        Log.i(tag, "Faculty list: $list")
                        val faculty = list.first()

                        timetableGateway
                            .getUniversityData(faculty.id)
                            .fold(
                                ifLeft = ::handleDataFailure,
                                ifRight = {
                                    Log.i(tag, "University info: $it")
                                }
                            )
                    }
                )*/
            /*subscriptionsInteractor
                .getAll()
                .fold(
                    ifLeft = ::handleDataFailure,
                    ifRight = { list ->
                        Log.i(tag, "Subscriptions: $list")

                        val subscription = list.firstOrNull()
                        if (subscription != null) {
                            subscriptionsInteractor
                                .getById(subscription.id)
                                .fold(
                                    ifLeft = ::handleDataFailure,
                                    ifRight = {
                                        assert(subscription == it)
                                        Log.i(tag, "select subscriptions done")

                                        subscriptionsInteractor
                                            .update(it.copy(title = "Subscription name"))
                                            .fold(
                                                ifLeft = ::handleRequestFailure,
                                                ifRight = {
                                                    Log.i(tag, "update subscription done")
                                                }
                                            )

                                        subscriptionsInteractor
                                            .deleteById(it.id)
                                            .fold(
                                                ifLeft = ::handleDataFailure,
                                                ifRight = {
                                                    Log.i(tag, "delete subscription done")
                                                }
                                            )

                                        timetableInteractor
                                            .getAllTimetables(it)
                                            .fold(
                                                ifLeft = ::handleDataFailure,
                                                ifRight = { list ->
                                                    Log.i(tag, "Timetables: $list")
                                                }
                                            )
                                    }
                                )
                        }
                    }
                )*/
        }
    }

    private fun <D> handleRequestFailure(requestFailure: RequestFailure<D>) {
        requestFailure.handle(
            ifData = { it.forEach(::handleDataFailure) },
            ifDomain = { Log.e(tag, "Domain fail: $it") }
        )
    }

    private fun handleDataFailure(dataFailure: DataFailure) {
        when (dataFailure) {
            is DataFailure.NotAuthenticated ->
                Log.e(tag, "Not authenticated: ${dataFailure.debugMessage}")

            is DataFailure.ConnectionToRepository ->
                Log.e(tag, "Connection to repository: ${dataFailure.debugMessage}")

            is DataFailure.UnknownResponse ->
                Log.e(tag, "Unknown response: body = ${dataFailure.body}, code = ${dataFailure.code}, message = ${dataFailure.debugMessage}")
        }
    }
}
