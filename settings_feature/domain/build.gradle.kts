plugins {
    `android-library`
    `kotlin-android`
}

apply<SharedGradlePlugin>()

android {
    namespace = "com.badrqaba.settings_feature.domain"
}

dependencies {
    core()
    hilt(project)
}