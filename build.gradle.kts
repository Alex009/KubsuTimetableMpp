val kotlinVersion = "1.3.50"

plugins {
    val kotlinVersion = "1.3.50"
    val toolsGradleVersion = "3.5.0"
    val sqldelightVersion = "1.2.0"

    kotlin("multiplatform").version(kotlinVersion).apply(false)
    kotlin("android").version(kotlinVersion).apply(false)
    id("com.android.library").version(toolsGradleVersion).apply(false)
    id("com.android.application").version(toolsGradleVersion).apply(false)
    id("com.squareup.sqldelight").version(sqldelightVersion).apply(false)
}

allprojects {
    ext["kotlinVersion"] = kotlinVersion
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://dl.bintray.com/florent37/maven")
    }

    // workaround for https://youtrack.jetbrains.com/issue/KT-27170
    configurations.create("compileClasspath")
}

tasks.register("clean", Delete::class).configure {
    delete(rootProject.buildDir)
}