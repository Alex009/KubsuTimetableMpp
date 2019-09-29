buildscript {
    val kotlinVersion = "1.3.30"
    extra["kotlinVersion"] = kotlinVersion
    setProperty("kotlinVersion", kotlinVersion)

    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.squareup.sqldelight:gradle-plugin:1.2.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }

    // workaround for https://youtrack.jetbrains.com/issue/KT-27170
    configurations.create("compileClasspath")
}

tasks.register("clean", Delete::class).configure {
    delete(rootProject.buildDir)
}