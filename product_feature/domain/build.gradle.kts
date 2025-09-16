plugins {
    `android-library`
    `kotlin-android`
}

apply<SharedGradlePlugin>()

android {
    namespace = "com.badrqaba.product_feature.domain"
}

dependencies {
    core()
    hilt(project)
}