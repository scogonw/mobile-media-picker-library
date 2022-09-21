package com.scogo.mediapicker.build.logic

import ANDROID_COMPILE_SDK_VERSION
import ANDROID_MIN_SDK_VERSION
import ANDROID_TARGET_SDK_VERSION
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get

@Suppress("UnstableApiUsage")
class ScogoAndroidLibPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply("com.android.library")
        target.plugins.apply("org.jetbrains.kotlin.android")
        target.plugins.apply("maven-publish")

        target.extensions.getByType(LibraryExtension::class.java).also {
            it.compileSdk = target.ANDROID_COMPILE_SDK_VERSION
            it.compileOptions {
                targetCompatibility = JavaVersion.VERSION_1_8
                sourceCompatibility = JavaVersion.VERSION_1_8
            }
            it.defaultConfig {
                minSdk = target.ANDROID_MIN_SDK_VERSION
                targetSdk = target.ANDROID_TARGET_SDK_VERSION
                multiDexEnabled = true
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
            it.publishing {
                singleVariant("release") {
                    withSourcesJar()
                    withJavadocJar()
                }
            }
        }

        target.afterEvaluate {
            target.extensions.getByType(PublishingExtension::class.java).also {
                it.publications.also { pub ->
                    pub.create<MavenPublication>("maven").also { maven ->
                        with(maven) {
                            groupId = "com.gitlab.scogo"
                            artifactId = "scogo_media_picker_library"
                            version = "1.0.2"
                            from(components["release"])
                        }
                    }
                }
            }
        }
    }
}