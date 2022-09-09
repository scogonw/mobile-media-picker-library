plugins {
    id("scogo_android_lib")
}
dependencies {
    implementation(project(":library:utils"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.android.paging)
}