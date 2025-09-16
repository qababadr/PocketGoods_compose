package com.badrqaba.product_feature.presentation.presentation.screen

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.badrqaba.core.data.local.converter.ControlledConverter
import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.data.mapper.toProductPreview
import com.badrqaba.core.util.api.ApiService
import com.badrqaba.core.util.api.mock.ApiMockEngine
import com.badrqaba.core.util.api.mock.MockData
import com.badrqaba.core.util.dispatcher.TestDispatchers
import com.badrqaba.core_ui.util.CURRENT_DESTINATION_KEY
import com.badrqaba.core_ui.util.PRODUCT_ID_KEY
import com.badrqaba.core_ui.util.Screen
import com.badrqaba.product_feature.data.repository.ProductRepositoryImpl
import com.badrqaba.product_feature.domain.use_case.GetProductUseCase
import com.badrqaba.product_feature.domain.use_case.GetProductsUseCase
import com.badrqaba.product_feature.domain.use_case.GetSuggestedProductsUseCase
import com.badrqaba.product_feature.domain.use_case.ProductUseCases
import com.badrqaba.product_feature.domain.use_case.SearchProductsUseCase
import com.badrqaba.product_feature.presentation.screen.ProductEvent
import com.badrqaba.product_feature.presentation.screen.ProductViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.runner.RunWith
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ProductViewModelTest {
    private lateinit var viewModel: ProductViewModel
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        testDispatchers = TestDispatchers()
        Dispatchers.setMain(testDispatchers.testDispatcher)

        val context = ApplicationProvider.getApplicationContext<Context>()

        val mockDatabase = Room
            .inMemoryDatabaseBuilder(context, PocketGoodsDatabase::class.java)
            .addTypeConverter(ControlledConverter(Gson()))
            .build()

        val mockApiService = ApiService(ApiMockEngine().get())

        val repository = ProductRepositoryImpl(
            database = mockDatabase,
            apiService = mockApiService
        )

        val useCases = ProductUseCases(
            getProducts = GetProductsUseCase(repository = repository),
            getProduct = GetProductUseCase(repository = repository),
            getSuggestedProducts = GetSuggestedProductsUseCase(repository = repository),
            searchProducts = SearchProductsUseCase(repository = repository)
        )

        val savedStateHandle = SavedStateHandle(
            mapOf(
                CURRENT_DESTINATION_KEY to Screen.HomeScreen.route,
                PRODUCT_ID_KEY to 1L
            )
        )

        viewModel = ProductViewModel(
            useCases = useCases,
            savedStateHandle = savedStateHandle,
            dispatcher = testDispatchers
        )
    }

    @Test
    fun getProducts_should_return_success_with_list_of_product_previews(): Unit = runTest {
        viewModel.onEvent(event = ProductEvent.GetProducts)

        viewModel.state.test {
            val loadingState = awaitItem()
            assertTrue(loadingState.isPageLoading)
            assertTrue(loadingState.products.isEmpty())

            val successState = awaitItem()
            assertFalse(successState.isPageLoading)
            assertTrue(successState.products.isNotEmpty())
            assertEquals(
                successState.products,
                MockData
                    .productsPaginationResponse
                    .data
                    .map { it.toProductPreview() }
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun loadProduct_should_return_the_correct_product_detail(): Unit = runTest {
        val correctProduct = MockData
            .sunGlassesProductResponse
            .data

        viewModel.onEvent(event = ProductEvent.LoadProduct)

        viewModel.state.test {
            val loadingState = awaitItem()
            assertTrue(loadingState.isPageLoading)
            assertNull(loadingState.product)

            val successState = awaitItem()
            assertFalse(successState.isPageLoading)
            assertNotNull(successState.product)
            assertEquals(successState.product, correctProduct)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun searchSuggestions_should_give_suggested_products(): Unit = runTest {
        val searchQuery = "s"

        viewModel.state.test {
            val initialState = awaitItem()
            assertEquals("", initialState.searchQuery)

            viewModel.onEvent(ProductEvent.OnQueryChange(query = searchQuery))
            viewModel.onEvent(ProductEvent.SearchSuggestions)

            val queryChangedState = awaitItem()
            assertEquals(searchQuery, queryChangedState.searchQuery)

            val loadingState = awaitItem()
            assertTrue(loadingState.isSearching)
            assertTrue(loadingState.suggestedProducts.isEmpty())

            val successState = awaitItem()
            assertFalse(successState.isSearching)
            assertTrue(successState.suggestedProducts.isNotEmpty())
            assertEquals(
                successState.suggestedProducts,
                MockData
                    .suggestedProducts(query = searchQuery)
                    .data
                    .map { it.toProductPreview() }
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun searchProducts_should_give_non_empty_products_matching_the_search_query(): Unit = runTest {
        val searchQuery = "s"

        viewModel.state.test {
            val initialState = awaitItem()
            assertEquals("", initialState.searchQuery)

            viewModel.onEvent(ProductEvent.OnQueryChange(query = searchQuery))
            viewModel.onEvent(ProductEvent.SearchProducts)

            val loadingState = awaitItem()
            assertTrue(loadingState.isPageLoading)
            assertTrue(loadingState.suggestedProducts.isEmpty())

            val successState = awaitItem()
            assertFalse(successState.isPageLoading)
            assertEquals(
                successState.products,
                MockData
                    .searchPaginationResponse(query = searchQuery)
                    .data
                    .map { it.toProductPreview() }
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}