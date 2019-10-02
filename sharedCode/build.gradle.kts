import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

repositories {
    google()
    jcenter()
    mavenCentral()
    maven(url = "https://dl.bintray.com/florent37/maven")
}

sqldelight {
    database("MyDatabase") {
        packageName = "com.kubsu.timetable.data.db"
    }
}

kotlin {
    android()
    // This is for iPhone emulator
    // Switch here to iosArm64 (or iosArm32) to build library for iPhone device
    iosX64("ios") {
        binaries {
            framework("Shared")
        }
    }

    sourceSets {
        val coroutineVersion = "1.3.2"
        val sqldelightVersion = "1.2.0"
        val kodeinVersion = "6.4.0"
        val serializationVersion = "0.13.0"
        val ktorVersion = "1.3.0-beta-1"

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                // Coroutine
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$coroutineVersion")

                // Network
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")

                // Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializationVersion")

                // Db
                implementation("com.squareup.sqldelight:runtime:$sqldelightVersion")

                // Di
                implementation("org.kodein.di:kodein-di-core:$kodeinVersion")
                implementation("org.kodein.di:kodein-di-erased:$kodeinVersion")

                // Preferences
                implementation("com.russhwolf:multiplatform-settings:0.3.3")

                // Time
                implementation("com.soywiz.korlibs.klock:klock:1.7.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                // Mock
                implementation("io.mockk:mockk-common:1.9.3")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))

                // Coroutine
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")

                // Network
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
                implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")

                // Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializationVersion")

                // Db
                implementation("com.squareup.sqldelight:android-driver:$sqldelightVersion")

                // Di
                implementation("org.kodein.di:kodein-di-framework-android-x:$kodeinVersion")

                // Sugar
                implementation("androidx.core:core-ktx:1.1.0")
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
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:$coroutineVersion")

                // Network
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization-native:$ktorVersion")
                implementation("io.ktor:ktor-client-logging-native:$ktorVersion")

                // Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serializationVersion")

                // Db
                implementation("com.squareup.sqldelight:ios-driver:$sqldelightVersion")
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

/*tasks.withType<Test> {
    useJUnitPlatform()
}*/

// It is necessay because we need to have access to context on CommonCode to use SQLDelight database
android {
    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(16)
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

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
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
// This task attaches native framework built from ios module to Xcode project
// (see iosApp directory). Don't run this task directly,
// Xcode runs this task itself during its build process.
// Before opening the project from iosApp directory in Xcode,
// make sure all Gradle infrastructure exists (gradle.wrapper, gradlew).
/*
task("copyFramework") {
    val buildType = project.findProperty("kotlin.build.type") as? String ?: "DEBUG"
    val framework = (kotlin.targets["ios"] as KotlinNativeTarget).compilations["main"].target.binaries.findFramework("Shared", buildType)!!
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
*/

/*
task("iosTest") {
    dependsOn("linkDebugTestIos")
    doLast {
        val testBinaryPath =
            (kotlin.targets["ios"] as KotlinNativeTarget).binaries.getTest("DEBUG").outputFile.absolutePath
        exec {
            commandLine("xcrun", "simctl", "spawn", "iPhone Xʀ", testBinaryPath)
        }
    }
}
tasks["check"].dependsOn("iosTest")*/
