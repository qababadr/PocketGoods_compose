package com.badrqaba.product_feature.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.badrqaba.core.data.local.converter.ControlledConverter
import com.badrqaba.core.data.local.dao.ProductDao
import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.data.mapper.toProductPreview
import com.badrqaba.core.util.api.ApiService
import com.badrqaba.core.util.api.Resource
import com.badrqaba.core.util.api.mock.ApiMockEngine
import com.badrqaba.core.util.api.mock.MockData
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ProductRepositoryImplTest {

    private lateinit var repository: ProductRepositoryImpl

    private lateinit var productDao: ProductDao

    @Before
    fun setUp() {

        val context = ApplicationProvider.getApplicationContext<Context>()

        val mockDatabase =
            Room.inMemoryDatabaseBuilder(
                context,
                PocketGoodsDatabase::class.java
            ).addTypeConverter(ControlledConverter(Gson()))
                .build()

        val mockApiService = ApiService(ApiMockEngine().get())

        productDao = mockDatabase.productDao()

        repository = ProductRepositoryImpl(
            database = mockDatabase,
            apiService = mockApiService
        )
    }

    @Test
    fun getProducts_should_return_success_with_list_of_product_previews(): Unit = runTest {
        val page = 1

        repository.getProducts(page = page).test {
            val loadingState = awaitItem()
            assert(loadingState is Resource.Loading)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            assertNotNull(successState.data)
            successState.data?.let { response ->
                assertTrue(response.data.isNotEmpty())
                assertTrue(
                    response.data.contains(
                        MockData.productsPaginationResponse.data.first().toProductPreview()
                    )
                )
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getProduct_should_return_the_correct_cached_product_detail(): Unit = runTest {
        val id = 1L

        repository.getProduct(id = id).test {
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            assertNotNull(successState.data)
            successState.data?.let { product ->
                val cachedProduct = productDao.getProduct(productId = id).first()
                assertEquals(product, MockData.sunGlassesProductResponse.data)
                assertNotNull(cachedProduct)
                assertEquals(product.id, cachedProduct.product.id)
                assertEquals(product.title, cachedProduct.product.title)
                assertEquals(product.quantity, cachedProduct.product.quantity)
                assertEquals(product.description, cachedProduct.product.description)
                assertEquals(product.category, cachedProduct.product.category)
                product.media.forEach { image ->
                    assertTrue(cachedProduct.images.any { it.original == image.original })
                }
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getProduct_should_return_error_when_passing_non_existing_product(): Unit = runTest {
        val id = 100L

        repository.getProduct(id = id).test {
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val errorSate = awaitItem()
            assertTrue(errorSate is Resource.Error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getSuggestedProducts_should_give_suggested_products(): Unit = runTest {
        val query = "s"

        repository.getSuggestedProducts(query = query).test {
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            assertNotNull(successState.data)
            successState.data?.let { suggestedProducts ->
                assertTrue(suggestedProducts.isNotEmpty())
                suggestedProducts.forEach { suggestedProduct ->
                    assertTrue(suggestedProduct.title.contains(query))
                }
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getSuggestedProducts_should_give_no_suggestion_when_passing_random_non_existing_product_title(): Unit =
        runTest {
            val query = "some rand text input"

            repository.getSuggestedProducts(query = query).test {
                val loadingState = awaitItem()
                assertTrue(loadingState is Resource.Loading)

                val successState = awaitItem()
                assertTrue(successState is Resource.Success)
                successState.data?.let { suggestedProducts ->
                    assertTrue(suggestedProducts.isEmpty())
                }


                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun searchProducts_should_give_non_empty_products_matching_the_search_query(): Unit = runTest {
        val query = "s"
        val page = 1

        repository.searchProducts(page = page, query = query).test {
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            successState.data?.let { response ->
                val products = response.data
                assertTrue(products.isNotEmpty())
                products.forEach { suggestedProduct ->
                    assertTrue(suggestedProduct.title.contains(query))
                }
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun searchProducts_should_give_empty_products_for_non_existing_products(): Unit = runTest {
        val query = "some non existing product title"
        val page = 1

        repository.searchProducts(page = page, query = query).test {
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            successState.data?.let { response ->
                val products = response.data
                assertTrue(products.isEmpty())
            }

            cancelAndIgnoreRemainingEvents()
        }
    }
}