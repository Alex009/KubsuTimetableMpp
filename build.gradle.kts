buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://maven.fabric.io/public")
    }
    dependencies {
        classpath(Libs.kotlin_gradle_plugin)
        classpath(Libs.kotlin_serialization)
        classpath(Libs.navigation_safe_args_gradle_plugin)
        classpath(Libs.com_android_tools_build_gradle)
        classpath(Libs.gradle_plugin)
        classpath(Libs.google_services)
        classpath(Libs.io_fabric_tools_gradle)
    }
}

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
}

plugins {
    id("de.fayard.refreshVersions") version Versions.de_fayard_refreshversions_gradle_plugin
}