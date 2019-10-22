import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("io.fabric")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.2")

    defaultConfig {
        val applicationName = "timetable"
        applicationId = "com.kubsu.$applicationName"

        minSdkVersion(16)
        targetSdkVersion(29)

        versionName = "0.1.0"
        versionCode = 1

        base.archivesBaseName = "${applicationName}_$versionName"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("release-key.jks")
            storePassword = "a3JhbnNvZGFyX2t1YnN1"
            keyAlias = "kubsu"
            keyPassword = "6b72616e736f6461725f6b75627375"
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }

    sourceSets {
        val main by getting {
            java.srcDirs("src/main/kotlin")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    androidExtensions {
        isExperimental = true
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":sharedCode"))
    implementation(fileTree("include" to "*.jar", "dir" to "libs"))
    implementation(Libs.kotlin_stdlib)

    // Android
    implementation(Libs.appcompat)
    implementation(Libs.constraintlayout)
    implementation(Libs.recyclerview)
    implementation(Libs.cardview)
    implementation(Libs.exifinterface)
    implementation(Libs.core_ktx)
    implementation(Libs.swiperefreshlayout)
    implementation(Libs.material)
    implementation(Libs.fragment_ktx)
    implementation(Libs.preference_ktx)

    // Firebase
    implementation(Libs.firebase_core)
    implementation(Libs.firebase_messaging)
    implementation(Libs.firebase_analytics)
    implementation(Libs.crashlytics)

    // Serialization
    implementation(Libs.kotlinx_serialization_runtime)

    // App Killer
    implementation(Libs.app_killer_manager)

    // Bottom sheet
    implementation(Libs.sheetmenu)

    // Navigation
    implementation(Libs.navigation_fragment_ktx)
    implementation(Libs.navigation_ui_ktx)

    // Teaco
    implementation(Libs.teaco_android)

    // DI
    implementation(Libs.kodein_di_core)
    implementation(Libs.kodein_di_erased)
    implementation(Libs.kodein_di_framework_android_x)

    // Coroutines
    implementation(Libs.kotlinx_coroutines_core)
    implementation(Libs.kotlinx_coroutines_android)

    // Klock
    implementation(Libs.klock_jvm)

    // Test
    testImplementation(Libs.junit_jupiter_api)
    androidTestImplementation(Libs.androidx_test_core)
    androidTestImplementation(Libs.junit)
    androidTestImplementation(Libs.androidx_test_runner)
    androidTestImplementation(Libs.espresso_core)
}
