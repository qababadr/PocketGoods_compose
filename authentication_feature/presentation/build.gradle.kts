import features_library.androidTestAuthenticationData
import features_library.authenticationFeatureDomain

plugins {
    alias(libs.plugins.kotlin.compose)
    `android-library`
    `kotlin-android`
}

apply<SharedGradlePlugin>()
android {
    namespace = "com.badrqaba.authentication_feature.presentation"

    buildFeatures {
        compose = true
    }
}

dependencies {
    hilt(project)
    core()

    authenticationFeatureDomain()

    compose(project)
    coreUI()

    androidTestAuthenticationData()
    presentationModuleAndroidTestImplementation(project)
}