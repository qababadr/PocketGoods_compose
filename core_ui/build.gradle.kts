plugins {
    `android-library`
    `kotlin-android`
    alias(libs.plugins.kotlin.compose)
}

apply<SharedGradlePlugin>()

android {
    namespace = "com.badrqaba.core_ui"

    buildFeatures {
        compose = true
    }
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    compose(project)
    hilt(project)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.ktor3)
    implementation(libs.accompanist.pager)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.dotlottie.android)

}