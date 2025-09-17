import features_library.wishlistFeatureDomain

plugins {
    `android-library`
    `kotlin-android`
}

apply<SharedGradlePlugin>()

android {
    namespace = "com.badrqaba.wishlist_feature.data"
}

dependencies {
    core()
    hilt(project)
    roomDatabase(project)

    dataModuleAndroidTestImplementation(project)
    wishlistFeatureDomain()
}