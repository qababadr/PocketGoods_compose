import features_library.productFeatureDomain

plugins {
    `android-library`
    `kotlin-android`
}

apply<SharedGradlePlugin>()

android {
    namespace = "com.badrqaba.product_feature.data"
}

dependencies {
    core()
    hilt(project)
    roomDatabase(project)
    dataModuleAndroidTestImplementation(project)
    productFeatureDomain()
}