plugins {
    id("scogo_android_compose_lib")
}
dependencies {
    api(project(":library:common:ui-res"))
    api(project(":library:common:ui-theme"))
    api(project(":library:common:ui-components"))
    api(project(":library:core"))
    api(project(":library:utils"))
    implementation(libs.bundles.android.ktx)
    implementation(libs.bundles.android.compose)
    implementation(libs.bundles.android.camerax)
    debugImplementation(libs.bundles.android.compose.debug)
    implementation(libs.android.compose.material.icons)
    implementation(libs.android.paging)
    implementation(libs.android.paging.compose)
    implementation(libs.android.accompanist.permissions)
    implementation(libs.android.accompanist.pager)
    implementation(libs.android.eventbus)
    api(libs.android.image.cropper)
}