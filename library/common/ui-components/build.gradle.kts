plugins {
    id("scogo_android_compose_lib")
}
dependencies {
    implementation(project(":library:core"))
    implementation(project(":library:common:ui-res"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.android.ktx)
    implementation(libs.bundles.android.compose)
    debugImplementation(libs.bundles.android.compose.debug)
    implementation(libs.android.compose.coil)
    implementation(libs.android.compose.material.icons)
    implementation(libs.android.glide)
    annotationProcessor(libs.android.glide.compiler)
    implementation(libs.android.paging)
    implementation(libs.android.paging.compose)

}