plugins {
    `kotlin-dsl`
}
repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}
dependencies {
    implementation(libs.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    compileOnly(gradleApi())
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}
tasks.withType<JavaCompile>().configureEach {
    options.release.set(8)
}
gradlePlugin {
    plugins {
        fun createPlugin(id: String, className: String) {
            plugins.create(id) {
                this.id = id
                implementationClass = className
            }
        }
        createPlugin(
            id = "scogo_android_lib",
            className = "com.scogo.mediapicker.build.logic.ScogoAndroidLibPlugin"
        )
        createPlugin(
            id = "scogo_android_compose_lib",
            className = "com.scogo.mediapicker.build.logic.ScogoAndroidComposeLibPlugin"
        )
    }
}