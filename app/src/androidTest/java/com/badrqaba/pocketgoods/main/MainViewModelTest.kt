package com.badrqaba.pocketgoods.main

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.badrqaba.authentication_feature.data.repository.AuthenticationRepositoryImpl
import com.badrqaba.authentication_feature.domain.repository.AuthenticationRepository
import com.badrqaba.authentication_feature.domain.use_case.AuthenticationUseCases
import com.badrqaba.authentication_feature.domain.use_case.ClearCachedUserUseCase
import com.badrqaba.authentication_feature.domain.use_case.GetAuthenticatedUserUseCase
import com.badrqaba.authentication_feature.domain.use_case.GetCachedAuthenticatedUser
import com.badrqaba.authentication_feature.domain.use_case.LoginUseCase
import com.badrqaba.authentication_feature.domain.use_case.LogoutUseCase
import com.badrqaba.authentication_feature.domain.use_case.RegisterUseCase
import com.badrqaba.authentication_feature.presentation.auth.AuthViewModel
import com.badrqaba.core.data.local.converter.ControlledConverter
import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.util.api.ApiService
import com.badrqaba.core.util.api.mock.ApiMockEngine
import com.badrqaba.core.util.api.mock.MockData
import com.badrqaba.core.util.crypto.mock.MockCryptoService
import com.badrqaba.core.util.dispatcher.TestDispatchers
import com.badrqaba.core_ui.util.CURRENT_DESTINATION_KEY
import com.badrqaba.core_ui.util.PRODUCT_ID_KEY
import com.badrqaba.core_ui.util.Screen
import com.badrqaba.settings_feature.data.repository.ApplicationSettingsRepositoryImpl
import com.badrqaba.settings_feature.domain.use_case.ApplicationSettingsUseCases
import com.badrqaba.settings_feature.domain.use_case.GetAppSettingsUseCase
import com.badrqaba.settings_feature.domain.use_case.SaveAppSettingsUseCase
import com.badrqaba.wishlist_feature.data.repository.WishlistRepositoryImpl
import com.badrqaba.wishlist_feature.domain.use_case.GetEntireWishlistUseCase
import com.badrqaba.wishlist_feature.domain.use_case.ToggleWishlistUseCase
import com.badrqaba.wishlist_feature.domain.use_case.WishlistUseCases
import com.google.gson.Gson
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var testDispatchers: TestDispatchers
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var settingsUseCases: ApplicationSettingsUseCases
    private lateinit var wishlistUseCases: WishlistUseCases
    private lateinit var mockDatabase: PocketGoodsDatabase
    private lateinit var authRepository: AuthenticationRepository
    private lateinit var authViewModel: AuthViewModel

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

        val mockCryptoService = MockCryptoService()

        savedStateHandle = SavedStateHandle(
            mapOf(
                CURRENT_DESTINATION_KEY to Screen.HomeScreen.route,
                PRODUCT_ID_KEY to 1L
            )
        )

        val settingsRepository = ApplicationSettingsRepositoryImpl(
            database = mockDatabase
        )
        val wishlistRepository = WishlistRepositoryImpl(
            database = mockDatabase,
            apiService = mockApiService,
            cryptoService = mockCryptoService
        )

        settingsUseCases = ApplicationSettingsUseCases(
            getApplicationSettings = GetAppSettingsUseCase(repository = settingsRepository),
            saveApplicationSettings = SaveAppSettingsUseCase(repository = settingsRepository)
        )

        wishlistUseCases = WishlistUseCases(
            toggleWishlist = ToggleWishlistUseCase(repository = wishlistRepository),
            getEntireWishlist = GetEntireWishlistUseCase(repository = wishlistRepository)
        )

        authRepository = AuthenticationRepositoryImpl(
            database = mockDatabase,
            apiService = mockApiService,
            cryptoService = mockCryptoService
        )

        val authUseCases = AuthenticationUseCases(
            login = LoginUseCase(repository = authRepository),
            register = RegisterUseCase(repository = authRepository),
            getAuthenticatedUser = GetAuthenticatedUserUseCase(repository = authRepository),
            clearCachedUser = ClearCachedUserUseCase(repository = authRepository),
            logout = LogoutUseCase(repository = authRepository),
            getCachedAuthenticatedUser = GetCachedAuthenticatedUser(repository = authRepository)
        )

        mainViewModel = MainViewModel(
            authenticationUseCases = authUseCases,
            settingsUseCase = settingsUseCases,
            dispatchers = testDispatchers,
            savedStateHandle = savedStateHandle,
            wishlistUseCases = wishlistUseCases
        )

        authViewModel = AuthViewModel(
            useCases = authUseCases,
            dispatchers = testDispatchers
        )
    }

    @Test
    fun loadData_should_load_data_and_clear_cached_user_when_failed_to_authenticate(): Unit =
        runTest {

            var onErrorIsCalled = false

            mainViewModel.onEvent(
                event = MainActivityEvent.OnLoadData(
                    onSuccess = {},
                    onError = { onErrorIsCalled = true }
                )
            )

            mainViewModel.state.test {
                val loadingState = awaitItem()
                assertTrue(loadingState.isSplashScreenVisible)

                val loadedState = awaitItem()
                assertFalse(loadedState.isSplashScreenVisible)
                assertTrue(onErrorIsCalled)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun loadData_should_load_data_including_cached_user(): Unit = runTest {
        settingsUseCases.saveApplicationSettings(
            settings = MockData.savedAppSetting
        )

        authRepository.login(
            email = MockData.MOCK_EMAIL,
            password = MockData.MOCK_PASSWORD
        ).test {
            var onErrorIsCalled = false

            mainViewModel.onEvent(
                event = MainActivityEvent.OnLoadData(
                    onSuccess = { user ->
                        assertTrue(user != null)
                    },
                    onError = { onErrorIsCalled = true }
                )
            )

            assertFalse(onErrorIsCalled)
        }
    }
}