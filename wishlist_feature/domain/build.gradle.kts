plugins {
    `android-library`
    `kotlin-android`
}

apply<SharedGradlePlugin>()

android {
    namespace = "com.badrqaba.wishlist_feature.domain"
}

dependencies {
    core()
    hilt(project)
}