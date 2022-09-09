plugins {
    id("scogo_android_compose_lib")
}
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.android.ktx)
    implementation(libs.bundles.android.compose)
    debugImplementation(libs.bundles.android.compose.debug)
    implementation(libs.android.compose.coil)
    implementation(libs.android.compose.material.icons)
}