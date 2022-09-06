plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    compileSdk = libs.versions.android.compile.sdk.get().toInt()

    defaultConfig {
        applicationId = "com.scogo.mediapicker"
        minSdk = libs.versions.android.min.sdk.get().toInt()
        targetSdk = libs.versions.android.target.sdk.get().toInt()
        versionCode = libs.versions.android.version.code.get().toInt()
        versionName = libs.versions.android.version.name.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro")
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.android.compose.compiler.get()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = libs.versions.kotlin.jvm.target.get()
    }
}

dependencies {
    implementation(project(":library:compose"))
    implementation(project(":library:utils"))
    testImplementation("junit:junit:4.13.2")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.app.compat)
    implementation(libs.android.material)
    implementation(libs.android.constraint.layout)
    implementation(libs.bundles.android.compose)
    debugImplementation(libs.bundles.android.compose.debug)
}