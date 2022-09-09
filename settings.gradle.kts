pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
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
