package com.kubsu.timetable.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import com.kubsu.timetable.R
import com.kubsu.timetable.utils.CrashlyticsLogger
import com.kubsu.timetable.utils.MaterialActionDialogCreator
import com.kubsu.timetable.utils.closeApp
import com.kubsu.timetable.utils.getCompatColor
import com.kubsu.timetable.utils.logics.DarkThemeStatus
import com.kubsu.timetable.utils.logics.Keyboard
import com.kubsu.timetable.utils.logics.PermissionProvider
import com.thelittlefireman.appkillermanager.*
import com.thelittlefireman.appkillermanager.exceptions.IntentListForActionNotFoundException
import com.thelittlefireman.appkillermanager.exceptions.IntentNotAvailableException
import com.thelittlefireman.appkillermanager.managers.DevicesManager
import com.thelittlefireman.appkillermanager.models.KillerManagerActionType
import kotlinx.android.synthetic.main.activity_main.*

class AppActivity : AppCompatActivity(), NavHost, CrashlyticsLogger {
    override fun getNavController(): NavController = findNavController(R.id.nav_host_fragment)

    private val alertCreator: MaterialActionDialogCreator?
    init {
        alertCreator = DevicesManager
            .getDevice()
            .fold(
                ifLeft = {
                    onFailure(it)
                    null
                },
                ifRight = { device ->
                    MaterialActionDialogCreator(device, this, ::onFailure)
                }
            )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DarkThemeStatus.applyPreviousTheme(applicationContext)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getCompatColor(R.color.colorPrimaryVariant)
            window.navigationBarColor = getCompatColor(android.R.color.transparent)
        }

        if (!PermissionProvider.hasAllPermissions(this))
            PermissionProvider.checkPermissions(this)

        Keyboard.observeKeyboardVisibleStatus(root_view)

        alertCreator?.run {
            showDialogForAction(
                KillerManagerActionType.ActionAutoStart,
                R.string.auto_start_message
            )

            showDialogForAction(
                KillerManagerActionType.ActionPowerSaving,
                R.string.battery_optimization_message
            )

            showDialogForAction(
                KillerManagerActionType.ActionNotifications,
                R.string.notification_message
            )
        }
    }

    private fun onFailure(failure: Failure) =
        when (failure) {
            is InternalFail -> {
                error(
                    tag = failure.tag,
                    exception = failure.exception,
                    message = failure.message
                )
            }

            is UnknownDeviceFail -> Unit

            is IntentFailure ->
                error(
                    exception = handleIntentFail(failure),
                    message = ""
                )
        }

    private fun handleIntentFail(intentFailure: IntentFailure): Exception? =
        when (intentFailure) {
            is IntentNotAvailableFail ->
                IntentNotAvailableException(
                    intentFailure.intent.component.toString()
                )

            is IntentListForActionNotFoundFail ->
                IntentListForActionNotFoundException(
                    intentFailure.debugInformation
                )
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        alertCreator?.onActivityResult(requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (!PermissionProvider.isAllPermissionsAllowed(requestCode, permissions, grantResults))
            closeApp()
    }
}