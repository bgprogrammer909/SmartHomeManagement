// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

// Only keep buildscript for plugins like Google Services
buildscript {
    dependencies {
        // Firebase Gradle plugin
        classpath("com.google.gms:google-services:4.4.0")

    }
}

// NO allprojects { repositories { ... } } here!
// Repositories should be defined in settings.gradle.kts
