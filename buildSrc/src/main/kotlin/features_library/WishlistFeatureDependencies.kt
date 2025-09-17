package features_library

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

fun DependencyHandler.wishlistFeatureDomain() {
    add("implementation", project(Modules.WISHLIST_DOMAIN))
}

fun DependencyHandler.androidTestWishlistData() {
    add("implementation", project(Modules.WISHLIST_DATA))
}

fun DependencyHandler.wishlistFeature() {
    add("implementation", project(Modules.WISHLIST_PRESENTATION))
    add("implementation", project(Modules.WISHLIST_DOMAIN))
    add("implementation", project(Modules.WISHLIST_DATA))
}