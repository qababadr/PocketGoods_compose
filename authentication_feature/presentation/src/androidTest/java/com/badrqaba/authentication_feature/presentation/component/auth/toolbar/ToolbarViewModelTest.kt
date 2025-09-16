package com.badrqaba.authentication_feature.presentation.component.auth.toolbar

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.badrqaba.authentication_feature.data.repository.AuthenticationRepositoryImpl
import com.badrqaba.authentication_feature.domain.use_case.AuthenticationUseCases
import com.badrqaba.authentication_feature.domain.use_case.ClearCachedUserUseCase
import com.badrqaba.authentication_feature.domain.use_case.GetAuthenticatedUserUseCase
import com.badrqaba.authentication_feature.domain.use_case.GetCachedAuthenticatedUser
import com.badrqaba.authentication_feature.domain.use_case.LoginUseCase
import com.badrqaba.authentication_feature.domain.use_case.LogoutUseCase
import com.badrqaba.authentication_feature.domain.use_case.RegisterUseCase
import com.badrqaba.authentication_feature.presentation.auth.AuthViewModel
import com.badrqaba.authentication_feature.presentation.auth.toolbar.ToolbarEvent
import com.badrqaba.authentication_feature.presentation.auth.toolbar.ToolbarViewModel
import com.badrqaba.core.data.local.converter.ControlledConverter
import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.util.api.ApiService
import com.badrqaba.core.util.api.mock.ApiMockEngine
import com.badrqaba.core.util.api.mock.MockData
import com.badrqaba.core.util.crypto.mock.MockCryptoService
import com.badrqaba.core.util.dispatcher.TestDispatchers
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertFalse
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ToolbarViewModelTest {
    private lateinit var viewModel: ToolbarViewModel
    private lateinit var testDispatchers: TestDispatchers
    private lateinit var mockDatabase: PocketGoodsDatabase

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

        val repository = AuthenticationRepositoryImpl(
            database = mockDatabase,
            apiService = mockApiService,
            cryptoService = mockCryptoService
        )

        val useCases = AuthenticationUseCases(
            getAuthenticatedUser = GetAuthenticatedUserUseCase(repository = repository),
            login = LoginUseCase(repository = repository),
            register = RegisterUseCase(repository = repository),
            logout = LogoutUseCase(repository = repository),
            clearCachedUser = ClearCachedUserUseCase(repository = repository),
            getCachedAuthenticatedUser = GetCachedAuthenticatedUser(repository = repository)
        )

        viewModel = ToolbarViewModel(
            useCases = useCases,
            dispatchers = testDispatchers
        )
    }

    @Test
    fun logout_should_sign_out_user(): Unit = runTest {

        var onErrorCalled = false

        viewModel.onEvent(event = ToolbarEvent.OnLogout(
            userId = MockData.userDTO.id,
            onLoggedOut = {},
            onError = {
                onErrorCalled = true
            }
        ))

        advanceUntilIdle()

        val cachedUser = mockDatabase
            .userDao()
            .getLatestCachedUser()
            .firstOrNull()
            ?.user

        assertNull(cachedUser)
        assertFalse(onErrorCalled)
    }
}