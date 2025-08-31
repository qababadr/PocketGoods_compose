plugins {
    `android-library`
    `kotlin-android`

    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlinx.serialization)
}

apply<SharedGradlePlugin>()

android {
    namespace = "com.badrqaba.core"
}

dependencies {
    roomDatabase(project)
    hilt(project)
    ktor(project)

    implementation(libs.androidx.security.crypto)
    implementation(libs.gson)

    implementation(libs.kotlinx.coroutines.test)
}