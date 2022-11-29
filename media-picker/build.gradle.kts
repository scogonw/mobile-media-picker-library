plugins {
    id("scogo_android_compose_lib")
    id("maven-publish")
}
android {
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}
dependencies {
    implementation(libs.bundles.android.ktx)
    implementation(libs.bundles.android.compose)
    implementation(libs.bundles.android.camerax)
    debugImplementation(libs.bundles.android.compose.debug)
    implementation(libs.bundles.android.accompanist)
    implementation(libs.android.compose.material.icons)
    implementation(libs.android.paging)
    implementation(libs.android.paging.compose)
    implementation(libs.android.eventbus)
    implementation(libs.android.compose.coil)
    implementation(libs.android.glide)
    annotationProcessor(libs.android.glide.compiler)
    implementation(libs.android.exo.player)
    api(libs.android.image.cropper)
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.gitlab.scogo"
                artifactId = "scogo_media_picker_library"
                version = libs.versions.scogo.media.picker.get()
                from(components["release"])
            }
        }
    }
}