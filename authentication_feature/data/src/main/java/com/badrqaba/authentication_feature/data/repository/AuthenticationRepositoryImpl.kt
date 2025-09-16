package com.badrqaba.authentication_feature.data.repository

import com.badrqaba.authentication_feature.domain.repository.AuthenticationRepository
import com.badrqaba.core.data.local.dao.ImageDao
import com.badrqaba.core.data.local.dao.ProductDao
import com.badrqaba.core.data.local.dao.UserDao
import com.badrqaba.core.data.local.dao.WishlistDao
import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.data.mapper.toImageEntity
import com.badrqaba.core.data.mapper.toProductEntity
import com.badrqaba.core.data.mapper.toUser
import com.badrqaba.core.data.mapper.toUserEntity
import com.badrqaba.core.data.mapper.toWishlistItemEntity
import com.badrqaba.core.data.remote.dto.LoginResponseDTO
import com.badrqaba.core.data.remote.dto.UserDTO
import com.badrqaba.core.domain.model.User
import com.badrqaba.core.util.api.ApiError
import com.badrqaba.core.util.api.ApiService
import com.badrqaba.core.util.api.Resource
import com.badrqaba.core.util.crypto.CryptoService
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class AuthenticationRepositoryImpl @Inject constructor(
    database: PocketGoodsDatabase,
    private val apiService: ApiService,
    private val cryptoService: CryptoService
) : AuthenticationRepository {

    private val userDao: UserDao = database.userDao()
    private val wishlistDao: WishlistDao = database.wishlistDao()
    private val productDao: ProductDao = database.productDao()
    private val imageDao: ImageDao = database.imageDao()

    override fun login(
        email: String,
        password: String
    ): Flow<User> = flow {
        try {
            val response = apiService.post<LoginResponseDTO, Map<String, String>>(
                url = "login",
                body = mapOf(
                    "email" to email,
                    "password" to password
                ),
                headers = mapOf("Content-Type" to "application/x-www-form-urlencoded")
            )

            val token = cryptoService.encrypt(data = response.token)

            val userDTO = response.user
            val wishlist = userDTO.wishlist

            wishlistDao.clearWishlist(userId = userDTO.id)

            wishlist.forEach { item ->
                item.productDetail?.let { productDTO ->
                    productDTO.media.forEach { imageDTO ->
                        imageDao.insertImage(
                            image = imageDTO.toImageEntity(modelId = productDTO.id)
                        )
                    }
                    productDao.insertProduct(
                        product = productDTO.toProductEntity()
                    )
                }

                wishlistDao.insertWishlist(
                    wishlist = item.toWishlistItemEntity(
                        userId = userDTO.id
                    )
                )
            }

            userDao.insertUser(
                user = userDTO.toUserEntity(
                    token = token
                )
            )

            emitAll(
                userDao.getCachedUserWithWishlist(
                    userId = userDTO.id
                ).map {
                    it?.toUser() ?: userDTO.toUser()
                }
            )
        } catch (exp: Exception) {
            throw exp
        }
    }

    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): String {
        try {
            return apiService.put<String, Map<String, String>>(
                url = "register",
                body = mapOf(
                    "name" to fullName,
                    "email" to email,
                    "password" to password,
                    "password_confirmation" to passwordConfirmation
                ),
                headers = mapOf("Content-Type" to "application/x-www-form-urlencoded")
            )
        } catch (exp: Exception) {
            throw exp
        }
    }

    override fun getAuthenticatedUser(): Flow<Resource<User?>> {
        return flow {
            emit(Resource.Loading())

            try {
                val cachedUser = userDao
                    .getLatestCachedUser()
                    .first()

                if (cachedUser == null) {
                    emit(Resource.Error(error = ApiError(message = "User not found")))
                    return@flow
                }

                val decryptedToken = cryptoService.decrypt(
                    data = cachedUser.user.token
                )

                val userDTO = apiService.get<UserDTO>(
                    url = "user/is-authenticated",
                    headers = mapOf(
                        "Authorization" to "Bearer $decryptedToken"
                    )
                )

                val wishlist = userDTO.wishlist

                wishlistDao.clearWishlist(userId = userDTO.id)

                wishlist.forEach { item ->
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
                        wishlist = item.toWishlistItemEntity(
                            userId = userDTO.id
                        )
                    )
                }

                userDao.insertUser(
                    user = userDTO.toUserEntity(
                        cachedUser.user.token
                    )
                )

                emitAll(
                    userDao.getCachedUserWithWishlist(userId = userDTO.id)
                        .map { data ->
                            Resource.Success(data = data?.toUser() ?: userDTO.toUser())
                        }
                )
            } catch (exp: Exception) {
                emit(Resource.Error(error = exp))
            }
        }
    }

    override fun getCachedAuthenticatedUser(userId: Long): Flow<User?> {
        return userDao
            .getCachedUserWithWishlist(userId = userId)
            .map { it?.toUser() }
    }

    override suspend fun logout(userId: Long): Boolean {
        try {
            val cachedUser =
                userDao.getCachedUserWithWishlist(userId = userId).first() ?: return false

            val decryptedToken = cryptoService.decrypt(data = cachedUser.user.token)

            val response = apiService.get<Boolean>(
                url = "logout",
                headers = mapOf(
                    "Authorization" to "Bearer $decryptedToken"
                )
            )

            userDao.clear(
                userId = cachedUser.user.id
            )

            return response
        } catch (exp: Exception) {
            throw exp
        }
    }

    override suspend fun clearCachedUser() {
        val cachedUser = userDao.getLatestCachedUser().first() ?: return
        userDao.clear(userId = cachedUser.user.id)
    }
}