allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://dl.bintray.com/florent37/maven")
        maven(url = "https://jitpack.io")
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://dl.bintray.com/indrih17/teaco")
        maven(url = "https://maven.fabric.io/public")
    }

    // workaround for https://youtrack.jetbrains.com/issue/KT-27170
    configurations.create("compileClasspath")
}

plugins {
    id("de.fayard.refreshVersions") version Versions.de_fayard_refreshversions_gradle_plugin

    kotlin("multiplatform") apply false
    kotlin("android") apply false
    kotlin("serialization") apply false
    id("com.android.library") apply false
    id("com.android.application") apply false
    id("androidx.navigation.safeargs.kotlin") apply false
    id("com.squareup.sqldelight") apply false
    id("google-services") apply false
    id("io.fabric") apply false
}