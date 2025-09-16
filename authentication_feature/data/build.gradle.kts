import features_library.authenticationFeatureDomain

plugins {
    `android-library`
    `kotlin-android`
}

apply<SharedGradlePlugin>()

android {
    namespace = "com.badrqaba.authentication_feature.data"
}

dependencies {
    core()
    hilt(project)
    roomDatabase(project)
    dataModuleAndroidTestImplementation(project)
    authenticationFeatureDomain()
}