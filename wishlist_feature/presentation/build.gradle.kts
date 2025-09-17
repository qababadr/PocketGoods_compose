import features_library.androidTestWishlistData
import features_library.wishlistFeatureDomain

plugins {
    alias(libs.plugins.kotlin.compose)
    `android-library`
    `kotlin-android`
}

apply<SharedGradlePlugin>()
android {
    namespace = "com.badrqaba.wishlist_feature.presentation"
    buildFeatures {
        compose = true
    }
}

dependencies {
    hilt(project)
    core()

    coreUI()
    compose(project)

    wishlistFeatureDomain()

    androidTestWishlistData()
    presentationModuleAndroidTestImplementation(project)

}