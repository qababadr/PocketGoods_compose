package features_library

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

fun DependencyHandler.productFeatureDomain() {
    add("implementation", project(Modules.PRODUCT_DOMAIN))
}

fun DependencyHandler.androidTestProductFeatureData() {
    add("androidTestImplementation", project(Modules.PRODUCT_DATA))
}

fun DependencyHandler.productFeature() {
    add("implementation", project(Modules.PRODUCT_PRESENTATION))
    add("implementation", project(Modules.PRODUCT_DOMAIN))
    add("implementation", project(Modules.PRODUCT_DATA))
}