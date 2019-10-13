import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.2")

    defaultConfig {
        val applicationName = "timetable"
        applicationId = "com.kubsu.$applicationName"

        minSdkVersion(16)
        targetSdkVersion(29)

        versionName = "1.0.0"
        versionCode = 1

        base.archivesBaseName = "${applicationName}_$versionName"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            //signingConfig = signingConfigs.getByName("release")
        }
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "12"
    }
}

dependencies {
    implementation(project(":sharedCode"))
    implementation(fileTree("include" to "*.jar", "dir" to "libs"))

    implementation(Libs.kotlin_stdlib)
    implementation(Libs.appcompat)

    // DI
    implementation(Libs.kodein_di_core)
    implementation(Libs.kodein_di_erased)
    implementation(Libs.kodein_di_framework_android_x)

    implementation(Libs.kotlinx_coroutines_core)
    implementation(Libs.kotlinx_coroutines_android)

    testImplementation(Libs.junit)
    androidTestImplementation(Libs.com_android_support_test_runner)
    androidTestImplementation(Libs.espresso_core)
}
