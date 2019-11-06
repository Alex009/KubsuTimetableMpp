package platform

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings

actual fun createSettingsFactory(platformArgs: PlatformArgs): Settings.Factory =
    AppleSettings.Factory()