package features_library

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

fun DependencyHandler.settingsFeatureDomain() {
    add("implementation", project(Modules.SETTINGS_DOMAIN))
}

fun DependencyHandler.settingsFeature() {
    add("implementation", project(Modules.SETTINGS_DOMAIN))
    add("implementation", project(Modules.SETTINGS_DATA))
}