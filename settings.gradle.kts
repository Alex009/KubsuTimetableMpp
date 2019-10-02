rootProject.name = "KubsuTimetable"

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.multiplatform",
                "org.jetbrains.kotlin.android" -> useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
                "org.jetbrains.kotlin.serialization" -> useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
                "com.android.library",
                "com.android.application" -> useModule("com.android.tools.build:gradle:${requested.version}")
                "com.squareup.sqldelight" -> useModule("com.squareup.sqldelight:gradle-plugin:${requested.version}")
            }
        }
    }

    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://dl.bintray.com/florent37/maven")
        jcenter()
    }
}

include(":sharedCode", ":androidApp")

enableFeaturePreview("GRADLE_METADATA")