
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
@Suppress("unstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.google.com")
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
}
rootProject.name = "ScogoMediaPicker"
enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("build-logic") {
    dependencySubstitution {
        substitute(module("com.scogo.mediapicker:build-logic"))
            .using(project(":"))
    }
}
include(":app")
include(":media-picker")