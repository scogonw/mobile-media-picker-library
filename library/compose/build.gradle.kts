plugins {
    id("scogo_android_compose_lib")
}
dependencies {
    implementation(project(":library:common:ui-res"))
    implementation(project(":library:common:ui-theme"))
    implementation(libs.bundles.android.compose)
    debugImplementation(libs.bundles.android.compose.debug)
    implementation(libs.bundles.android.camerax)
    implementation(libs.android.compose.material.icons)
    implementation(libs.android.accompanist.permissions)
    implementation(libs.android.compose.coil)
}