package com.badrqaba.core.util.api.mock

import com.google.gson.Gson
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpResponseData
import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode

fun MockRequestHandleScope.paginateProducts(responseHeaders: Headers): HttpResponseData {
    return respond(
        Gson().toJson(MockData.productsPaginationResponse),
        HttpStatusCode.OK,
        responseHeaders
    )
}

fun MockRequestHandleScope.getProductDetails(responseHeaders: Headers): HttpResponseData {
    return respond(
        Gson().toJson(MockData.sunGlassesProductResponse),
        HttpStatusCode.OK,
        responseHeaders
    )
}

fun MockRequestHandleScope.searchSuggestion(data: MockEngineRequestData, responseHeaders: Headers): HttpResponseData {
    return if (data.method == "POST") {
        val searchQuery = data.formData["search_query"] ?: ""
        respond(
            Gson().toJson(MockData.suggestedProducts(query = searchQuery)),
            HttpStatusCode.OK,
            responseHeaders
        )
    } else {
        error("Unsupported Method ${data.method} for url ${data.url}")
    }
}

fun MockRequestHandleScope.searchProducts(data: MockEngineRequestData, responseHeaders: Headers): HttpResponseData {
    return if (data.method == "POST") {
        val searchQuery = data.formData["search_query"] ?: ""
        respond(
            Gson().toJson(MockData.searchPaginationResponse(query = searchQuery)),
            HttpStatusCode.OK,
            responseHeaders
        )
    } else {
        error("Unsupported Method ${data.method} for url ${data.url}")
    }
}