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
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":sharedCode"))

    val kotlinVersion = rootProject.ext["kotlinVersion"] as String
    val coroutineVersion = "1.3.2"
    val kodeinVersion = "6.4.0"
    implementation(fileTree("include" to "*.jar", "dir" to "libs"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("com.android.support.constraint:constraint-layout:1.1.3")

    implementation("org.kodein.di:kodein-di-core:$kodeinVersion")
    implementation("org.kodein.di:kodein-di-erased:$kodeinVersion")
    implementation("org.kodein.di:kodein-di-framework-android-x:$kodeinVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")

    testImplementation("junit:junit:4.12")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
}
