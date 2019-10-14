rootProject.name = "KubsuTimetable"

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.multiplatform", "org.jetbrains.kotlin.android" ->
                    useModule(Libs.kotlin_gradle_plugin)

                "org.jetbrains.kotlin.serialization" ->
                    useModule(Libs.kotlin_serialization)

                "androidx.navigation.safeargs.kotlin" ->
                    useModule(Libs.navigation_safe_args_gradle_plugin)

                "com.android.library", "com.android.application" ->
                    useModule(Libs.com_android_tools_build_gradle)

                "com.squareup.sqldelight" ->
                    useModule(Libs.gradle_plugin)

                "google-services" ->
                    useModule(Libs.google_services)

                "io.fabric" ->
                    useModule(Libs.io_fabric_tools_gradle)
            }
        }
    }

    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://maven.fabric.io/public")
    }
}

include(":sharedCode", ":androidApp")

enableFeaturePreview("GRADLE_METADATA")