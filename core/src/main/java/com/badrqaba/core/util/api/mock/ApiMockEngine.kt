package com.badrqaba.core.util.api.mock

import com.badrqaba.core.util.matchesRoute
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.ContentType
import io.ktor.http.headersOf

class ApiMockEngine {
    fun get() = client.engine

    private val responseHeaders =
        headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))

    private val client = HttpClient(MockEngine) {
        engine {
            addHandler { request ->

                val data = prepareRequestData(request = request)

                when {
                    data.path.contains("/api/products") ->
                        paginateProducts(responseHeaders = responseHeaders)

                    data.path == "/api/product/details/1" ->
                        getProductDetails(responseHeaders = responseHeaders)

                    data.path == "/api/product/search-suggestions" ->
                        searchSuggestion(data = data, responseHeaders = responseHeaders)

                    data.path == "/api/product/search" ->
                        searchProducts(data = data, responseHeaders = responseHeaders)

                    data.path == "/api/login" ->
                        login(data = data, responseHeaders = responseHeaders)

                    data.path == "/api/register" ->
                        register(data = data, responseHeaders = responseHeaders)

                    data.path == "/api/user/is-authenticated" ->
                        isAuthenticated(data = data, responseHeaders = responseHeaders)

                    data.path == "/api/logout" ->
                        logout(data = data, responseHeaders = responseHeaders)

                    data.path.matchesRoute("/api/wishlist/toggle/{id}") ->
                        toggleWishlist(data = data, responseHeaders = responseHeaders)

                    data.path == "/api/wishlist/list" ->
                        getEntireWishlist(data = data, responseHeaders = responseHeaders)

                    else -> error("The path ${data.url} is not defined")
                }

            }
        }
    }
}
