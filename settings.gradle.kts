rootProject.name = "KubsuTimetable"

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.multiplatform", "org.jetbrains.kotlin.android" ->
                    useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.org_jetbrains_kotlin}")

                "org.jetbrains.kotlin.serialization" ->
                    useModule("org.jetbrains.kotlin:kotlin-serialization:${Versions.org_jetbrains_kotlin}")

                "com.android.library", "com.android.application" ->
                    useModule("com.android.tools.build:gradle:${Versions.com_android_tools_build_gradle}")

                "com.squareup.sqldelight" ->
                    useModule("com.squareup.sqldelight:gradle-plugin:${Versions.com_squareup_sqldelight}")
            }
        }
    }

    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
    }
}

include(":sharedCode", ":androidApp")

enableFeaturePreview("GRADLE_METADATA")