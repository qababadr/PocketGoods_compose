import features_library.authenticationFeature
import features_library.productFeature
import features_library.settingsFeature
import features_library.wishlistFeature

plugins {
    id("com.android.application")
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = ProjectConfig.NAMESPACE
    compileSdk = ProjectConfig.COMPILE_SDK

    defaultConfig {
        applicationId = ProjectConfig.APPLICATION_ID
        minSdk = ProjectConfig.MIN_SDK
        targetSdk = ProjectConfig.TARGET_SDK
        versionCode = ProjectConfig.VERSION_CODE
        versionName = ProjectConfig.VERSION_NAME

        testInstrumentationRunner = ProjectConfig.TEST_INSTRUMENTATION_RUNNER
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = ProjectConfig.JVM_TARGET
    }

    composeOptions {
        kotlinCompilerExtensionVersion = ProjectConfig.KOTLIN_COMPILER_EXTENSION_VERSION
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    compose(project)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.espresso.idling.resource)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.ktor.client.mock)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    hilt(project)

    roomDatabase(project)
    roomDatabaseTesting(project)

    //coil
    implementation(libs.coil.compose)

    implementation(libs.accompanist.pager)

    implementation(libs.androidx.core.splashscreen)

    ktor(project)

    core()
    coreUI()
    productFeature()
    authenticationFeature()
    wishlistFeature()
    settingsFeature()

    testImplementation(kotlin("test"))
}

hilt {
    enableAggregatingTask = false
}