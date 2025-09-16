package com.badrqaba.authentication_feature.presentation.component.auth

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.badrqaba.authentication_feature.data.repository.AuthenticationRepositoryImpl
import com.badrqaba.authentication_feature.domain.use_case.AuthenticationUseCases
import com.badrqaba.authentication_feature.domain.use_case.ClearCachedUserUseCase
import com.badrqaba.authentication_feature.domain.use_case.GetAuthenticatedUserUseCase
import com.badrqaba.authentication_feature.domain.use_case.GetCachedAuthenticatedUser
import com.badrqaba.authentication_feature.domain.use_case.LoginUseCase
import com.badrqaba.authentication_feature.domain.use_case.LogoutUseCase
import com.badrqaba.authentication_feature.domain.use_case.RegisterUseCase
import com.badrqaba.authentication_feature.presentation.auth.AuthViewModel
import com.badrqaba.authentication_feature.presentation.auth.login.LoginFormEvent
import com.badrqaba.authentication_feature.presentation.auth.register.RegisterFormEvent
import com.badrqaba.core.data.local.converter.ControlledConverter
import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.data.mapper.toUser
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
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class AuthViewModelTest {
    private lateinit var viewModel: AuthViewModel
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

        viewModel = AuthViewModel(
            useCases = useCases,
            dispatchers = testDispatchers
        )
    }

    @Test
    fun login_should_authenticate_user(): Unit = runTest {
        viewModel.onLoginEvent(
            event = LoginFormEvent.OnEmailChange(
                email = MockData.MOCK_EMAIL
            )
        )

        viewModel.onLoginEvent(
            event = LoginFormEvent.OnPasswordChange(
                password = MockData.MOCK_PASSWORD
            )
        )

        advanceUntilIdle()

        viewModel.onLoginEvent(
            event = LoginFormEvent.Login(
            onError = { throwable ->
                assertNull(throwable)
            }
        ))

        advanceUntilIdle()

        viewModel.authState.test {
            awaitItem()

            val state = awaitItem()
            val cachedUser = mockDatabase
                .userDao()
                .getLatestCachedUser()
                .firstOrNull()
                ?.toUser()

            assertNotNull(state.authenticatedUser)
            assertEquals(state.authenticatedUser, cachedUser)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun login_should_fail_after_passing_wrong_credentials(): Unit = runTest {
        viewModel.onLoginEvent(
            event = LoginFormEvent.OnEmailChange(
                email = "some_wrong_email@email.email"
            )
        )

        viewModel.onLoginEvent(
            event = LoginFormEvent.OnPasswordChange(
                password = "Some password@ 1 23"
            )
        )

        advanceUntilIdle()

        viewModel.onLoginEvent(
            event = LoginFormEvent.Login(
                onError = { error ->
                    assertNotNull(error)
                }
            )
        )
    }

    @Test
    fun register_should_signup_user(): Unit = runTest {
        val username = "some user name"
        viewModel.onRegisterEvent(
            event = RegisterFormEvent.OnFirstAndLastNameChange(
                firstAndLastName = username
            )
        )
        viewModel.onRegisterEvent(
            event = RegisterFormEvent.OnEmailChange(email = MockData.MOCK_EMAIL)
        )
        viewModel.onRegisterEvent(
            event = RegisterFormEvent.OnPasswordChange(password = MockData.MOCK_PASSWORD)
        )
        viewModel.onRegisterEvent(
            event = RegisterFormEvent.OnConfirmPasswordChange(password = MockData.MOCK_PASSWORD)
        )


        viewModel.onRegisterEvent(
            event = RegisterFormEvent.Register(
                onRegistered = { registeredUsername ->
                    assertTrue(registeredUsername.isNotEmpty())
                    assertEquals(registeredUsername, username)
                },
                onError = {
                    fail("expecting no errors but got one")
                }
            )
        )

        advanceUntilIdle()
    }
}