package com.badrqaba.core.util.api.mock

import com.badrqaba.core.data.remote.dto.ImageDTO
import com.badrqaba.core.data.remote.dto.LoginResponseDTO
import com.badrqaba.core.data.remote.dto.ProductDTO
import com.badrqaba.core.data.remote.dto.ProductPreviewDTO
import com.badrqaba.core.data.remote.dto.UserDTO
import com.badrqaba.core.data.remote.dto.WishlistItemDTO
import com.badrqaba.core.data.remote.dto.pagination.PaginationLinksDTO
import com.badrqaba.core.data.remote.dto.pagination.PaginationMetaDTO
import com.badrqaba.core.data.remote.dto.pagination.PaginationResponseDTO
import com.badrqaba.core.domain.model.AppSettings
import com.badrqaba.core.domain.model.Image
import com.badrqaba.core.domain.model.Product
import com.badrqaba.core.util.api.ApiResponse

object MockData {

    const val MOCK_EMAIL = "email@email.email"

    const val MOCK_PASSWORD = "Password@1"
    const val TOKEN = "28|zzpMGvjoMKDlu3PfJX7rLydMLNMtODuy3dOqjoqRea0ca3fe"
    private const val BASE_URL = "http://192.168.1.6:8000"

    val userDTO = UserDTO(
        id = 6,
        name = "badr qaba",
        email = "backupbadr@gmail.com",
        emailVerifiedAt = null,
        wishlist = listOf(
            WishlistItemDTO(
                id = 7,
                productId = 3,
                productDetail = ProductDTO(
                    id = 3,
                    title = "Headphones",
                    category = "Electronics",
                    price = 129.99,
                    quantity = 150,
                    description = "Noise-cancelling over-ear headphones.",
                    media = listOf(
                        ImageDTO(
                            uuid = "eebfe1cc-2891-4bec-91ce-74def6c7a98d",
                            filename = "headphones-814055_1280image-by-stephanie-robertson-from-pixabay.jpg",
                            preview = "$BASE_URL/media/5/conversions/headphones-814055_1280image-by-stephanie-robertson-from-pixabay-thumbnail.jpg",
                            original = "$BASE_URL/media/5/headphones-814055_1280image-by-stephanie-robertson-from-pixabay.jpg"
                        ),
                        ImageDTO(
                            uuid = "d6c9ca2a-e300-4231-9278-70693694fce4",
                            filename = "image-by-dmitrijs-bojarovs-from-pixabay.jpg",
                            preview = "$BASE_URL/media/6/conversions/image-by-dmitrijs-bojarovs-from-pixabay-thumbnail.jpg",
                            original = "$BASE_URL/media/6/image-by-dmitrijs-bojarovs-from-pixabay.jpg"
                        )
                    )
                )
            ),
            WishlistItemDTO(
                id = 12,
                productId = 1,
                productDetail = ProductDTO(
                    id = 1,
                    title = "Sunglasses",
                    category = "Accessories",
                    price = 20.99,
                    quantity = 100,
                    description = "Stylish sunglasses for a sunny day.",
                    media = listOf(
                        ImageDTO(
                            uuid = "9837ee14-758a-4052-97cd-83c9ee4c2fdc",
                            filename = "aviator-sunglasses-2592111_1280_image-by-sandeep-handa-from-pixabay.jpg",
                            preview = "$BASE_URL/media/1/conversions/aviator-sunglasses-2592111_1280_image-by-sandeep-handa-from-pixabay-thumbnail.jpg",
                            original = "$BASE_URL/media/1/aviator-sunglasses-2592111_1280_image-by-sandeep-handa-from-pixabay.jpg"
                        ),
                        ImageDTO(
                            uuid = "48423fd0-5b18-4604-846f-3c9e72d19056",
                            filename = "sunglasses-178151_1280image-by-pawet-ludzinski-from-pixabay.jpg",
                            preview = "$BASE_URL/media/2/conversions/sunglasses-178151_1280image-by-pawet-ludzinski-from-pixabay-thumbnail.jpg",
                            original = "$BASE_URL/media/2/sunglasses-178151_1280image-by-pawet-ludzinski-from-pixabay.jpg"
                        )
                    )
                )
            )
        )
    )

    val productsPaginationResponse = PaginationResponseDTO(
        data = listOf(
            ProductPreviewDTO(
                1,
                "Sunglasses",
                "Accessories",
                20.99,
                "$BASE_URL/media/1/conversions/aviator-sunglasses-2592111_1280_image-by-sandeep-handa-from-pixabay-thumbnail.jpg"
            ),
            ProductPreviewDTO(
                2,
                "Parfum",
                "Beauty",
                49.99,
                "$BASE_URL/media/3/conversions/fragonard-1007437_1280image-by-nathaly-durepaire-from-pixabay-thumbnail.jpg"
            ),
            ProductPreviewDTO(
                3,
                "Headphones",
                "Electronics",
                129.99,
                "$BASE_URL/media/5/conversions/headphones-814055_1280image-by-stephanie-robertson-from-pixabay-thumbnail.jpg"
            ),
            ProductPreviewDTO(
                4,
                "Makeup",
                "Beauty",
                19.99,
                "$BASE_URL/media/7/conversions/image-by-aerngaoey-from-pixabay-thumbnail.jpg"
            ),
            ProductPreviewDTO(
                5,
                "Wrist Watch",
                "Accessories",
                149.99,
                "$BASE_URL/media/9/conversions/image-by-emamul-andalib-from-pixabay-thumbnail.jpg"
            ),
            ProductPreviewDTO(
                6,
                "Coffee",
                "Grocery",
                9.99,
                "$BASE_URL/media/11/conversions/coffee-5568374-1280-thumbnail.jpg"
            ),
            ProductPreviewDTO(
                7,
                "Lipstick",
                "Beauty",
                24.99,
                "$BASE_URL/media/13/conversions/image-by-steve-jang-from-pixabay-thumbnail.jpg"
            ),
            ProductPreviewDTO(
                8,
                "Microphone",
                "Electronics",
                79.99,
                "$BASE_URL/media/15/conversions/image-by-la88au88ra-from-pixabay-thumbnail.jpg"
            ),
            ProductPreviewDTO(
                9,
                "Washing Machine Liquid",
                "Home",
                7.99,
                "$BASE_URL/media/17/conversions/image-by-martins2018-from-pixabay-thumbnail.jpg"
            )
        ),
        links = PaginationLinksDTO(
            first = "$BASE_URL/api/products?page=1",
            last = "$BASE_URL/api/products?page=2",
            prev = null,
            next = "$BASE_URL/api/products?page=2"
        ),
        meta = PaginationMetaDTO(
            currentPage = 1,
            from = 1,
            lastPage = 2,
            path = "$BASE_URL/api/products",
            perPage = 9,
            to = 9,
            total = 12,
        )
    )

    val sunGlassesProductResponse = ApiResponse(
        data = Product(
            id = 1,
            title = "Sunglasses",
            category = "Accessories",
            price = 20.99,
            quantity = 100,
            description = "Stylish sunglasses for a sunny day.",
            media = listOf(
                Image(
                    uuid = "9837ee14-758a-4052-97cd-83c9ee4c2fdc",
                    filename = "aviator-sunglasses-2592111_1280_image-by-sandeep-handa-from-pixabay.jpg",
                    preview = "$BASE_URL/media/1/conversions/aviator-sunglasses-2592111_1280_image-by-sandeep-handa-from-pixabay-thumbnail.jpg",
                    original = "$BASE_URL/media/1/aviator-sunglasses-2592111_1280_image-by-sandeep-handa-from-pixabay.jpg"
                ),
                Image(
                    uuid = "48423fd0-5b18-4604-846f-3c9e72d19056",
                    filename = "sunglasses-178151_1280image-by-pawet-ludzinski-from-pixabay.jpg",
                    preview = "$BASE_URL/media/2/conversions/sunglasses-178151_1280image-by-pawet-ludzinski-from-pixabay-thumbnail.jpg",
                    original = "$BASE_URL/media/2/sunglasses-178151_1280image-by-pawet-ludzinski-from-pixabay.jpg"
                )
            )
        )
    )

    fun suggestedProducts(query: String) = ApiResponse(
        data = productsPaginationResponse.data.filter { it.title.contains(query) }
    )

    fun searchPaginationResponse(query: String): PaginationResponseDTO<ProductPreviewDTO> {
        val data = productsPaginationResponse.data.filter { it.title.contains(query) }
        return PaginationResponseDTO(
            data = data,
            links = PaginationLinksDTO(
                first = "$BASE_URL/api/products?page=1",
                last = "$BASE_URL/api/products?page=2",
                prev = null,
                next = "$BASE_URL/api/products?page=2"
            ),
            meta = PaginationMetaDTO(
                currentPage = 1,
                from = 1,
                lastPage = 2,
                path = "$BASE_URL/api/products",
                perPage = 9,
                to = 9,
                total = data.size,
            )
        )
    }

    val loginResponse = ApiResponse(
        data = LoginResponseDTO(
            user = userDTO,
            token = TOKEN
        )
    )

    fun registerResponse(
        fullName: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): ApiResponse<String?> {
        val data =
            if (email.isNotEmpty()
                && fullName.isNotEmpty()
                && password.isNotEmpty()
                && passwordConfirmation.isNotEmpty()
                && password == passwordConfirmation
            ) {
                "Registered"
            } else {
                null
            }
        return ApiResponse(data = data)
    }

    fun authenticatedUserResponse(hasCorrectToken: Boolean) = ApiResponse(
        data = if (hasCorrectToken) userDTO else null
    )

    fun logoutResponse(hasCorrectToken: Boolean) = ApiResponse(
        data = if (hasCorrectToken) true else null
    )

    const val INSERTED_WISHLIST_ITEM_ID = 13L

    val savedAppSetting = AppSettings(
        isDarkMode = true
    )
}