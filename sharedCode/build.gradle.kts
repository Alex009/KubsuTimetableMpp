import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("kotlinx-serialization")
    id("com.android.library")
    id("kotlin-android-extensions")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("MyDatabase") {
        packageName = "com.kubsu.timetable.data.db"
    }
}

kotlin {
    // Android
    android()

    // iOS
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64
    iOSTarget("ios") {
        binaries {
            framework("Shared")
        }
    }

    // Cocoa pods
    version = "0.1.11"
    cocoapods {
        // Configure fields required by CocoaPods.
        summary = "Kubsu timetable"
        homepage = "https://github.com/indrih17/KubsuTimetableMpp"
    }

    // Sources
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                // Coroutine
                implementation(Libs.kotlinx_coroutines_core_common)

                // Network
                implementation(Libs.ktor_client_core)
                implementation(Libs.ktor_client_serialization)
                implementation(Libs.ktor_client_logging)
                implementation(Libs.ktor_client_features)

                // Serialization
                implementation(Libs.kotlinx_serialization_runtime_common)

                // Db
                implementation(Libs.com_squareup_sqldelight_runtime)
                implementation(Libs.coroutines_extensions)

                // Presentation logic
                implementation(Libs.teaco_mpp)

                // Di
                implementation(Libs.kodein_di_core)
                implementation(Libs.kodein_di_erased)

                // Preferences
                implementation(Libs.multiplatform_settings)

                // Time
                implementation(Libs.klock)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                // Coroutines
                implementation(Libs.kotlinx_coroutines_core_common)
                implementation(Libs.kotlinx_coroutines_test)

                // Mock
                implementation(Libs.mockk_common)
                implementation(Libs.mockk)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))

                // Coroutine
                implementation(Libs.kotlinx_coroutines_android)

                // Network
                implementation(Libs.ktor_client_android)
                implementation(Libs.ktor_client_serialization_jvm)
                implementation(Libs.ktor_client_logging_jvm)

                // Serialization
                implementation(Libs.kotlinx_serialization_runtime)

                // Db
                implementation(Libs.android_driver)

                // Di
                implementation(Libs.kodein_di_framework_android_x)

                // Sugar
                implementation(Libs.core_ktx)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        val iosMain by getting {
            dependencies {
                // Coroutine
                implementation(Libs.kotlinx_coroutines_core_native)

                // Network
                implementation(Libs.ktor_client_ios)
                implementation(Libs.ktor_client_serialization_native)
                implementation(Libs.ktor_client_logging_native)

                // Serialization
                implementation(Libs.kotlinx_serialization_runtime_native)

                // Db
                implementation(Libs.ios_driver)
            }
        }
        val iosTest by getting {
        }

        all {
            languageSettings.apply {
                this.enableLanguageFeature("InlineClasses") // language feature name
                useExperimentalAnnotation("kotlin.Experimental") // annotation FQ-name
                progressiveMode = true // false by default
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

// It is necessay because we need to have access to context on CommonCode to use SQLDelight database
android {
    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(21)
    }

    // By default the android gradle plugin expects to find the kotlin source files in
    // the folder `main` and the test in the folder `test`. This is to be able place
    // the source code files inside androidMain and androidTest folders
    sourceSets {
        val main by getting {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/res")
        }
        val test by getting {
            java.srcDirs("src/androidTest/kotlin")
            res.srcDirs("src/androidTest/res")
        }
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions.unitTests.isIncludeAndroidResources = true
}

// This task attaches native framework built from ios module to Xcode project
// (see iosApp directory). Don't run this task directly,
// Xcode runs this task itself during its build process.
// Before opening the project from iosApp directory in Xcode,
// make sure all Gradle infrastructure exists (gradle.wrapper, gradlew).
task("copyFramework") {
    val buildType = project.findProperty("kotlin.build.type") as? String ?: "DEBUG"
    val framework =
        (kotlin.targets["ios"] as KotlinNativeTarget).compilations["main"].target.binaries.findFramework(
            "Shared",
            buildType
        )!!
    dependsOn(framework.linkTask)

    doLast {
        val srcFile = framework.outputFile
        val targetDir = project.property("configuration.build.dir") as? String ?: ""
        copy {
            from(srcFile.parent)
            into(targetDir)
            include("Shared.framework/**")
            include("Shared.framework.dSYM")
        }
    }
}

task("iosTest") {
    dependsOn("linkDebugTestIos")
    doLast {
        val testBinaryPath =
            (kotlin.targets["ios"] as KotlinNativeTarget).binaries.getTest("DEBUG")
                .outputFile.absolutePath
        exec {
            commandLine("xcrun", "simctl", "spawn", "iPhone Xʀ", testBinaryPath)
        }
    }
}


// workaround for https://youtrack.jetbrains.com/issue/KT-27170
configurations {
    create("compileClasspath")
}