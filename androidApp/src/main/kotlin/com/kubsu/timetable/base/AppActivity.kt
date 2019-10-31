package com.kubsu.timetable.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import com.kubsu.timetable.R
import com.kubsu.timetable.firebase.CrashlyticsLogger
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

class AppActivity : AppCompatActivity(), NavHost {
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

        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            // Night mode is not active, we're using the light theme.
            Configuration.UI_MODE_NIGHT_NO -> {
                // For 23 api and higher, the status bar will be light with dark content.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = getCompatColor(android.R.color.black)
                }
            }

            // Night mode is active, we're using dark theme.
            Configuration.UI_MODE_NIGHT_YES -> Unit
        }


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
                CrashlyticsLogger.error(
                    tag = failure.tag,
                    exception = failure.exception,
                    message = failure.message
                )
            }

            is UnknownDeviceFail -> Unit

            is IntentFailure ->
                CrashlyticsLogger.error(
                    exception = handleIntentFail(failure),
                    message = "IntentFailure",
                    tag = "AppActivity"
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
