package com.badrqaba.core_ui.util

sealed class Screen(val route: String) {
    data object HomeScreen: Screen(route = "home_screen")
    data object ProductDetailScreen: Screen(route = "product/{$PRODUCT_ID_KEY}")
    data object WishlistManagerScreen: Screen(route = "wishlist_manager_screen")
    data object ProductSearchResultScreen: Screen(route = "product/search/{$SEARCH_QUERY_KEY}")
    data object UnAuthorizedScreen: Screen(route = "unauthorized_screen")

    fun <T> path(vararg args: Pair<String, T>): String {
        var finalRoute = route
        args.forEach { (key, value) ->
            finalRoute = finalRoute.replace("{$key}", value.toString())
        }
        return finalRoute
    }
}