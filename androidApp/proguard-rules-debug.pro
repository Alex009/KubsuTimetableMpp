-printmapping mapping.txt

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
-keep class com.kubsu.timetable.domain.entity.* { *; }
-keep class com.kubsu.timetable.data.network.dto.* { *; }
-keep class com.kubsu.timetable.data.network.client.update.** { *; }
-keep class com.kubsu.timetable.data.db.diff.** { *; }
-keep class com.kubsu.timetable.presentation.timetable.** { *; }

# Ktor
-keep class io.ktor.** { *; }
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.atomicfu.**
-dontwarn io.netty.**
-dontwarn com.typesafe.**
-dontwarn org.slf4j.**