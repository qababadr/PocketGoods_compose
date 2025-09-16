package features_library

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

fun DependencyHandler.authenticationFeatureDomain() {
    add("implementation", project(Modules.AUTH_DOMAIN))
}

fun DependencyHandler.androidTestAuthenticationData() {
    add("implementation", project(Modules.AUTH_DATA))
}

fun DependencyHandler.authenticationFeature() {
    add("implementation", project(Modules.AUTH_PRESENTATION))
    add("implementation", project(Modules.AUTH_DOMAIN))
    add("implementation", project(Modules.AUTH_DATA))
}