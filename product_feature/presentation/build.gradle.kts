import features_library.productFeatureDomain
import features_library.androidTestProductFeatureData

plugins {
    alias(libs.plugins.kotlin.compose)
    `android-library`
    `kotlin-android`
}

apply<SharedGradlePlugin>()

android {
    namespace = "com.badrqaba.product_feature.presentation"

    buildFeatures {
        compose = true
    }
}

dependencies {
    productFeatureDomain()
    hilt(project)
    core()

    compose(project)
    coreUI()
    implementation(libs.coil.compose)

    androidTestProductFeatureData()
    presentationModuleAndroidTestImplementation(project)
}