
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
include(":library")
include(":library:core")
include(":library:utils")
include(":library:compose")
include(":library:common")
include(":library:common:ui-res")
include(":library:common:ui-theme")
include(":library:common:ui-components")
