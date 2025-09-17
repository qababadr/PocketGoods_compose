package com.badrqaba.wishlist_feature.presentation.screen

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.badrqaba.core.data.local.converter.ControlledConverter
import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.data.mapper.toUserEntity
import com.badrqaba.core.data.mapper.toWishlistItem
import com.badrqaba.core.util.api.ApiService
import com.badrqaba.core.util.api.mock.ApiMockEngine
import com.badrqaba.core.util.api.mock.MockData
import com.badrqaba.core.util.crypto.CryptoService
import com.badrqaba.core.util.crypto.mock.MockCryptoService
import com.badrqaba.core.util.dispatcher.TestDispatchers
import com.badrqaba.wishlist_feature.data.repository.WishlistRepositoryImpl
import com.badrqaba.wishlist_feature.domain.use_case.GetEntireWishlistUseCase
import com.badrqaba.wishlist_feature.domain.use_case.ToggleWishlistUseCase
import com.badrqaba.wishlist_feature.domain.use_case.WishlistUseCases
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class WishlistManagerViewModelTest {
    private lateinit var viewModel: WishlistManagerViewModel
    private lateinit var testDispatchers: TestDispatchers
    private lateinit var mockDatabase: PocketGoodsDatabase
    private lateinit var mockCryptoService: CryptoService

    private val user = MockData.userDTO
    private val correctWishlist = MockData
        .loginResponse
        .data
        .user
        .wishlist
        .map { it.toWishlistItem() }

    @Before
    fun setUp() {
        testDispatchers = TestDispatchers()
        Dispatchers.setMain(testDispatchers.testDispatcher)

        val context = ApplicationProvider.getApplicationContext<Context>()

        mockDatabase = Room
            .inMemoryDatabaseBuilder(context, PocketGoodsDatabase::class.java)
            .addTypeConverter(ControlledConverter(Gson()))
            .build()

        val mockApiService = ApiService(ApiMockEngine().get())

        mockCryptoService = MockCryptoService()

        val repository = WishlistRepositoryImpl(
            database = mockDatabase,
            apiService = mockApiService,
            cryptoService = mockCryptoService
        )

        val useCases = WishlistUseCases(
            getEntireWishlist = GetEntireWishlistUseCase(repository = repository),
            toggleWishlist = ToggleWishlistUseCase(repository = repository)
        )

        viewModel = WishlistManagerViewModel(
            useCases = useCases,
            dispatchers = testDispatchers
        )
    }

    @Test
    fun getWishlistItems_should_get_entire_wishlist_for_the_current_user(): Unit = runTest {
        mockDatabase
            .userDao()
            .insertUser(
                user = user.toUserEntity(
                    token = mockCryptoService.encrypt(
                        data = MockData.TOKEN
                    )
                )
            )

        viewModel.onEvent(
            event = WishlistManagerScreenEvent.GetWishlistItems(user.id)
        )

        advanceUntilIdle()

        viewModel.state.test {
            val loadingState = awaitItem()
            assertTrue(loadingState.isPageLoading)

            val successState = awaitItem()
            assertFalse(successState.isPageLoading)
            assertEquals(successState.wishlist, correctWishlist)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteWishlistItem_should_delete_product_from_wishlist(): Unit = runTest {
        viewModel.onEvent(
            event = WishlistManagerScreenEvent.GetWishlistItems(user.id)
        )

        advanceUntilIdle()

        val selectedWishlistItem = correctWishlist.first()

        viewModel.state.test {
            viewModel.onEvent(
                WishlistManagerScreenEvent.ShowDeleteConfirmationModal(selectedWishlistItem)
            )

            viewModel.onEvent(
                event = WishlistManagerScreenEvent.DeleteWishlistItem(
                    userId = user.id,
                    onSuccess = { productId ->
                        assertTrue(productId.isNotEmpty())
                    },
                    onError = { exp, _ ->
                        fail("Excepting no error but we got the exception ${exp?.message ?: ""}")
                    }
                )
            )

            advanceUntilIdle()

            val afterDeleteState = awaitItem()
            assertFalse(afterDeleteState.isDeleting)

            awaitItem()

            val refreshState = awaitItem()
            assertFalse(
                refreshState
                    .wishlist
                    .any { it.productId == selectedWishlistItem.productId }
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}