plugins {
    id("scogo_android_compose_lib")
}
dependencies {
    implementation(project(":library:common:ui-res"))
    implementation(project(":library:common:ui-theme"))
    implementation(project(":library:core"))
    implementation(project(":library:utils"))
    implementation(libs.bundles.android.ktx)
    implementation(libs.bundles.android.compose)
    debugImplementation(libs.bundles.android.compose.debug)
    implementation(libs.android.compose.material.icons)
    implementation(libs.android.compose.coil)
    implementation(libs.bundles.android.camerax)
    implementation(libs.android.paging)
    implementation(libs.android.paging.compose)
    implementation(libs.android.accompanist.permissions)
}