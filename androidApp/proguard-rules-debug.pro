# Save from obfuscation
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep class * extends java.lang.Exception
-keep class * extends kotlin.Throwable

# Crashlitics
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

# Kotlinx.serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class com.kubsu.timetable.**$$serializer { *; }
-keep class kotlin.reflect.** { *; }
-dontwarn kotlin.reflect.**
-keep class org.jetbrains.** { *; }

# R8
-keep class com.kubsu.timetable.presentation.timetable.model.* { *; }
-keep class com.kubsu.timetable.domain.entity.* { *; }
-keep class com.kubsu.timetable.data.network.dto.* { *; }
-keep class com.kubsu.timetable.data.network.client.update.** { *; }
-keep class com.kubsu.timetable.data.db.diff.** { *; }

# Ktor
-keep class io.ktor.** { *; }
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.atomicfu.**
-dontwarn io.netty.**
-dontwarn com.typesafe.**
-dontwarn org.slf4j.**