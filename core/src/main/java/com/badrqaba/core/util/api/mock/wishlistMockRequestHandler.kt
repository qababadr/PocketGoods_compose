package com.badrqaba.core.util.api.mock

import com.badrqaba.core.data.remote.dto.WishlistResponseDTO
import com.badrqaba.core.util.api.ApiResponse
import com.google.gson.Gson
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpResponseData
import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode

fun MockRequestHandleScope.toggleWishlist(
    data: MockEngineRequestData,
    responseHeaders: Headers
): HttpResponseData {
    val productId = data.path.substringAfterLast("/").toLongOrNull()
    val actualToken = data.headers["Authorization"]
    return if (actualToken == "Bearer ${MockData.TOKEN}") {

        val updatedWishlist = MockData.userDTO.wishlist.filter { it.productId != productId }

        val wishlistItemId = if (updatedWishlist.size < MockData.userDTO.wishlist.size) {
            -1L
        } else {
            MockData.INSERTED_WISHLIST_ITEM_ID
        }
        respond(
            Gson().toJson(
                ApiResponse(
                    data = WishlistResponseDTO(
                        inWishlist = false,
                        wishlistItemId = wishlistItemId
                    )
                )
            ),
            HttpStatusCode.OK,
            responseHeaders
        )
    } else {
        respond(
            Gson().toJson(ApiResponse(data = null)),
            HttpStatusCode.Unauthorized,
            responseHeaders
        )
    }
}

fun MockRequestHandleScope.getEntireWishlist(
    data: MockEngineRequestData,
    responseHeaders: Headers
): HttpResponseData {
    val actualToken = data.headers["Authorization"]
    return if (actualToken == "Bearer ${MockData.TOKEN}") {
        respond(
            Gson().toJson(ApiResponse(
                data = MockData.userDTO.wishlist
            )),
            HttpStatusCode.OK,
            responseHeaders
        )
    } else {
        respond(
            Gson().toJson(ApiResponse(data = null)),
            HttpStatusCode.Unauthorized,
            responseHeaders
        )
    }
}