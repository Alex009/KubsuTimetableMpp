import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    id("androidx.navigation.safeargs.kotlin")
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
    implementation(Libs.recyclerview)
    implementation(Libs.cardview)
    implementation(Libs.exifinterface)
    implementation(Libs.core_ktx)
    implementation(Libs.swiperefreshlayout)
    implementation(Libs.material)
    implementation(Libs.fragment_ktx)

    // Bottom sheet
    implementation(Libs.sheetmenu)

    // Navigation
    implementation(Libs.navigation_fragment_ktx)
    implementation(Libs.navigation_ui_ktx)

    // Teaco
    implementation(Libs.android_driver)

    // DI
    implementation(Libs.kodein_di_core)
    implementation(Libs.kodein_di_erased)
    implementation(Libs.kodein_di_framework_android_x)

    // Coroutines
    implementation(Libs.kotlinx_coroutines_core)
    implementation(Libs.kotlinx_coroutines_android)

    // Test
    testImplementation(Libs.junit_jupiter_api)
    androidTestImplementation(Libs.androidx_test_core)
    androidTestImplementation(Libs.junit)
    androidTestImplementation(Libs.androidx_test_runner)
    androidTestImplementation(Libs.espresso_core)
}
