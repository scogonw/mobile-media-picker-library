// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress(
    "DSL_SCOPE_VIOLATION",
    "MISSING_DEPENDENCY_CLASS",
    "UNRESOLVED_REFERENCE_WRONG_RECEIVER",
    "FUNCTION_CALL_EXPECTED",
    "UnstableApiUsage"
)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
}
buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
        classpath("com.scogo.mediapicker:build-logic")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}
tasks {
    register("clean",Delete::class) {
        delete(rootProject.buildDir)
    }
}
