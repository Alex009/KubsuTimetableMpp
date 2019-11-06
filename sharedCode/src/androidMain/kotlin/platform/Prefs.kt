package platform

import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings

actual fun createSettingsFactory(platformArgs: PlatformArgs): Settings.Factory =
    AndroidSettings.Factory(platformArgs.context)