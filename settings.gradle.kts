pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()

        maven {
            url = uri("https://jitpack.io")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "PocketGoods"
include(":app")
include(":core")
include(":core_ui")
include(":product_feature:domain")
include(":product_feature:data")
include(":product_feature:presentation")
include(":authentication_feature:domain")
include(":authentication_feature:data")
include(":authentication_feature:presentation")
include(":wishlist_feature:domain")
include(":wishlist_feature:data")
include(":wishlist_feature:presentation")
