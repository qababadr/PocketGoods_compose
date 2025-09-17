import features_library.settingsFeatureDomain

plugins {
    `android-library`
    `kotlin-android`
}

apply<SharedGradlePlugin>()
android {
    namespace = "com.badrqaba.settings_feature.data"
}

dependencies {
    core()
    hilt(project)
    roomDatabase(project)

    dataModuleAndroidTestImplementation(project)
    settingsFeatureDomain()
}