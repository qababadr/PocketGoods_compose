package com.badrqaba.wishlist_feature.data.repository

import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.data.local.entity.WishlistItemEntity
import com.badrqaba.core.data.mapper.toImageEntity
import com.badrqaba.core.data.mapper.toProductEntity
import com.badrqaba.core.data.mapper.toWishlistItem
import com.badrqaba.core.data.mapper.toWishlistItemEntity
import com.badrqaba.core.data.remote.dto.WishlistItemDTO
import com.badrqaba.core.data.remote.dto.WishlistResponseDTO
import com.badrqaba.core.domain.model.WishlistItem
import com.badrqaba.core.util.api.ApiError
import com.badrqaba.core.util.api.ApiService
import com.badrqaba.core.util.api.Resource
import com.badrqaba.core.util.crypto.CryptoService
import com.badrqaba.wishlist_feature.domain.repository.WishlistRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class WishlistRepositoryImpl @Inject constructor(
    database: PocketGoodsDatabase,
    private val apiService: ApiService,
    private val cryptoService: CryptoService
) : WishlistRepository {

    private val wishlistDao = database.wishlistDao()
    private val userDao = database.userDao()
    private val productDao = database.productDao()
    private val imageDao = database.imageDao()

    override suspend fun toggleWishlist(userId: Long, productId: Long): Long {
        val cachedUser = userDao
            .getCachedUserWithWishlist(userId = userId)
            .first() ?: throw ApiError(message = "No user found")

        val decryptedToken = cryptoService.decrypt(data = cachedUser.user.token)

        try {
            val response = apiService.get<WishlistResponseDTO>(
                url = "wishlist/toggle/${productId}",
                headers = mapOf(
                    "Authorization" to "Bearer $decryptedToken"
                )
            )

            if (response.wishlistItemId == -1L) {
                wishlistDao.deleteWishlistItem(
                    productId = productId,
                    userId = userId
                )
            } else {
                wishlistDao.insertWishlist(
                    wishlist = WishlistItemEntity(
                        id = response.wishlistItemId,
                        productId = productId,
                        userId = userId
                    )
                )
            }

            return response.wishlistItemId
        } catch (exp: Exception) {
            throw exp
        }
    }

    override fun getEntireWishlist(userId: Long): Flow<Resource<List<WishlistItem>>> {
        return flow {
            val cachedUser = userDao
                .getCachedUserWithWishlist(userId = userId)
                .first()

            if (cachedUser == null) {
                emit(
                    value = Resource.Error(
                        error = ApiError(message = "No user found")
                    )
                )
                return@flow
            }

            emit(
                Resource.Loading(
                    data = cachedUser.wishlist.map { it.toWishlistItem() }
                )
            )

            try {
                val decryptedToken = cryptoService.decrypt(data = cachedUser.user.token)

                val list = apiService.get<List<WishlistItemDTO>>(
                    url = "wishlist/list",
                    headers = mapOf(
                        "Authorization" to "Bearer $decryptedToken"
                    )
                )

                wishlistDao.clearWishlist(userId = cachedUser.user.id)

                list.forEach { item ->
                    item.productDetail?.let { dto ->
                        dto.media.forEach { imageDTO ->
                            imageDao.insertImage(
                                image = imageDTO.toImageEntity(modelId = dto.id)
                            )
                        }
                        productDao.insertProduct(
                            product = dto.toProductEntity()
                        )
                    }

                    wishlistDao.insertWishlist(
                        wishlist = item.toWishlistItemEntity(userId = cachedUser.user.id)
                    )
                }

                emitAll(
                    userDao.getCachedUserWithWishlist(
                        userId = userId
                    ).map { data ->
                        Resource.Success(
                            data = data
                                ?.wishlist
                                ?.map { it.toWishlistItem() }
                                ?: list.map { it.toWishlistItem() }
                        )
                    }
                )

            } catch (exp: Exception) {
                emit(Resource.Error(error = exp))
            }
        }
    }
}