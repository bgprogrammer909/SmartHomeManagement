// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

// Only keep buildscript for plugins that require the old classpath method (e.g., Google Services)
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Firebase Gradle plugin
        classpath("com.google.gms:google-services:4.4.0")
    }
}

// Note:
// Do NOT include 'allprojects { repositories { ... } }' here!
// Repositories should be defined in settings.gradle.kts using the new preferred approach.
