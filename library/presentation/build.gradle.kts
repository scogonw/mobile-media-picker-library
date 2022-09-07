plugins {
    id("scogo_android_compose_lib")
}
dependencies {
    implementation(project(":library:common:ui-theme"))
    implementation(project(":library:compose"))
    implementation(project(":library:core"))
    implementation(project(":library:utils"))
    implementation(libs.bundles.android.compose)
    debugImplementation(libs.bundles.android.compose.debug)
}