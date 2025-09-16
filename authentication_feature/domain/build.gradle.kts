plugins {
    `android-library`
    `kotlin-android`
}

apply<SharedGradlePlugin>()
android {
    namespace = "com.badrqaba.authentication_feature.domain"
}

dependencies {
    core()
    hilt(project)
}