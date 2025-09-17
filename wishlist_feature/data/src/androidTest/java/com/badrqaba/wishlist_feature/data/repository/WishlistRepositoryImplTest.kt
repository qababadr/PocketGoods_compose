package com.badrqaba.wishlist_feature.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.badrqaba.core.data.local.converter.ControlledConverter
import com.badrqaba.core.data.local.dao.ImageDao
import com.badrqaba.core.data.local.dao.ProductDao
import com.badrqaba.core.data.local.dao.UserDao
import com.badrqaba.core.data.local.dao.WishlistDao
import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.data.mapper.toImageEntity
import com.badrqaba.core.data.mapper.toProductEntity
import com.badrqaba.core.data.mapper.toUserEntity
import com.badrqaba.core.data.mapper.toWishlistItemEntity
import com.badrqaba.core.util.api.ApiError
import com.badrqaba.core.util.api.ApiService
import com.badrqaba.core.util.api.Resource
import com.badrqaba.core.util.api.mock.ApiMockEngine
import com.badrqaba.core.util.api.mock.MockData
import com.badrqaba.core.util.crypto.CryptoService
import com.badrqaba.core.util.crypto.mock.MockCryptoService
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class WishlistRepositoryImplTest {
    private lateinit var mockApiService: ApiService
    private lateinit var mockDatabase: PocketGoodsDatabase
    private lateinit var mockCryptoService: CryptoService
    private lateinit var repositoryImpl: WishlistRepositoryImpl
    private lateinit var userDao: UserDao
    private lateinit var wishlistDao: WishlistDao
    private lateinit var productDao: ProductDao
    private lateinit var imageDao: ImageDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        mockDatabase = Room
            .inMemoryDatabaseBuilder(context, PocketGoodsDatabase::class.java)
            .addTypeConverter(ControlledConverter(Gson()))
            .build()

        mockApiService = ApiService(ApiMockEngine().get())

        mockCryptoService = MockCryptoService()

        userDao = mockDatabase.userDao()
        wishlistDao = mockDatabase.wishlistDao()
        productDao = mockDatabase.productDao()
        imageDao = mockDatabase.imageDao()

        repositoryImpl = WishlistRepositoryImpl(
            database = mockDatabase,
            apiService = mockApiService,
            cryptoService = mockCryptoService
        )
    }

    @Test
    fun toggleWishlist_should_remove_product_from_user_wishlist(): Unit = runTest {
        val productId = 1L
        val userDTO = MockData.userDTO
        val product = MockData
            .sunGlassesProductResponse
            .data

        userDao.insertUser(
            user = userDTO.toUserEntity(
                token = mockCryptoService.encrypt(
                    data = MockData.TOKEN
                )
            )
        )

        productDao.insertProduct(product = product.toProductEntity())

        product.media.forEach {
            imageDao.insertImage(image = it.toImageEntity(modelId = product.id))
        }

        userDTO.wishlist.forEach { item ->
            wishlistDao.insertWishlist(
                wishlist = item.toWishlistItemEntity(userId = userDTO.id)
            )
        }

        val response = repositoryImpl.toggleWishlist(
            productId = productId,
            userId = userDTO.id
        )

        assertEquals(response, -1L)

        val wishlist = wishlistDao
            .getWishlist(userId = userDTO.id)
            .first()

        assertFalse(
            wishlist.any { it.productId == productId }
        )
    }

    @Test
    fun toggleWishlist_should_add_product_to_user_wishlist(): Unit = runTest {
        val productId = MockData
            .productsPaginationResponse
            .data[1]
            .id

        val userDTO = MockData.userDTO

        userDao.insertUser(
            user = userDTO.toUserEntity(
                token = mockCryptoService.encrypt(
                    data = MockData.TOKEN
                )
            )
        )

        val response = repositoryImpl.toggleWishlist(
            productId = productId,
            userId = userDTO.id
        )

        assertEquals(response, MockData.INSERTED_WISHLIST_ITEM_ID)

        val wishlist = wishlistDao
            .getWishlist(userId = userDTO.id)
            .first()

        assertTrue(
            wishlist.any { it.productId == productId }
        )
    }

    @Test
    fun toggleWishlist_should_throw_error_when_user_is_not_authenticated(): Unit = runTest {
        val productId = MockData
            .productsPaginationResponse
            .data[1]
            .id

        val userId = MockData.userDTO.id

        try {
            repositoryImpl.toggleWishlist(
                productId = productId,
                userId = userId
            )
        } catch (exp: Exception) {
            assertTrue(exp is ApiError)
        }
    }

    @Test
    fun getEntireWishlist_should_get_the_entire_user_wishlist_with_images(): Unit = runTest {
        val userDTO = MockData.userDTO
        val product = MockData.sunGlassesProductResponse.data

        userDao.insertUser(
            user = userDTO.toUserEntity(
                token = mockCryptoService.encrypt(
                    data = MockData.TOKEN
                )
            )
        )

        productDao.insertProduct(product = product.toProductEntity())

        product.media.forEach {
            imageDao.insertImage(image = it.toImageEntity(modelId = product.id))
        }

        userDTO.wishlist.forEach { item ->
            wishlistDao.insertWishlist(
                wishlist = item.toWishlistItemEntity(userId = userDTO.id)
            )
        }

        repositoryImpl.getEntireWishlist(userId = userDTO.id).test {
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)
            assertNotNull(loadingState.data)
            assertTrue(loadingState.data!!.isNotEmpty())
            loadingState.data?.forEach { item ->
                if (item.productId == product.id) {
                    assertNotNull(item.productDetail)
                }
            }

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            assertNotNull(successState.data)
            assertTrue(successState.data!!.isNotEmpty())
            loadingState.data?.forEach { item ->
                if(item.productId == product.id) {
                    assertNotNull(item.productDetail)
                }
            }

            cancelAndIgnoreRemainingEvents()
        }
    }
}