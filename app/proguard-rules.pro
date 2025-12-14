# Add project specific ProGuard rules here.
-keep class com.whatsappdownloader.** { *; }
-keepclassmembers class com.whatsappdownloader.** { *; }

# Keep data classes
-keep class com.whatsappdownloader.data.model.** { *; }

# Coil
-keep public class * implements coil.request.RequestDelegate
-keep class coil.** { *; }
-dontwarn coil.**
