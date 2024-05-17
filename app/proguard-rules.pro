# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep Retrofit annotations
-keepattributes *Annotation*
-keepattributes Signature

# Keep Retrofit interfaces and their methods
-keep interface retrofit2.** {
    *;
}

# Keep Retrofit classes that are used as parameters or return types in the API interface methods
-keep class vn.aris.baseappcompose.data.models.** {*;}
-keep class vn.aris.baseappcompose.data.base.** {*;}


# Keep enums used by Retrofit
-keep enum retrofit2.** {
    *;
}

# Keep methods that are used for JSON serialization/deserialization
-keepclassmembers class * {
    @retrofit2.http.* <methods>;
}

# If you're using Gson for JSON serialization/deserialization
-keep class com.google.gson.** { *; }

# If you're using Moshi for JSON serialization/deserialization
-keep class com.squareup.moshi.** { *; }

# Keep OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep class okio.** { *; }

# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
 -keep,allowobfuscation,allowshrinking interface retrofit2.Call
 -keep,allowobfuscation,allowshrinking class retrofit2.Response

 # With R8 full mode generic signatures are stripped for classes that are not
 # kept. Suspend functions are wrapped in continuations where the type argument
 # is used.
 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

#Event bus
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

#Joda time
-keep class org.joda.time.** { *; }
-keepclassmembers class org.joda.time.** { *; }

-dontwarn com.google.errorprone.annotations.MustBeClosed
-ignorewarnings