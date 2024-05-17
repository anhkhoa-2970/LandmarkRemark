// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("lifecycle_version", "2.6.2")
        set("retrofit_version", "2.9.0")
        set("moshi_version", "1.14.0")
        set("ok_httpclient_version", "4.12.0")
        set("calendar_version", "2.0.4")
        set("kotlin_version", "2.0.4")
        set("hilt_version", "2.50")
        set("coroutines_version", "1.8.0")
        set("joda_time_version", "2.12.7")
        set("permissionX_version", "1.7.1")
        set("event_bus_version", "3.3.1")
        set("room_version", "2.6.1")
        set("glide_version", "1.0.0-beta01")
        set("systemuicontroller_version", "0.34.0")
    }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // add classpath here
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("androidx.room") version "2.6.1" apply false

}