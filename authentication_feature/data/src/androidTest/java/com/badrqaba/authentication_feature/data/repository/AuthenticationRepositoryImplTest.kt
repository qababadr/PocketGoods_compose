package com.badrqaba.authentication_feature.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.badrqaba.core.data.local.converter.ControlledConverter
import com.badrqaba.core.data.local.dao.UserDao
import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.data.mapper.toUser
import com.badrqaba.core.data.mapper.toUserEntity
import com.badrqaba.core.util.api.ApiError
import com.badrqaba.core.util.api.ApiService
import com.badrqaba.core.util.api.Resource
import com.badrqaba.core.util.api.mock.ApiMockEngine
import com.badrqaba.core.util.api.mock.MockData
import com.badrqaba.core.util.crypto.CryptoService
import com.badrqaba.core.util.crypto.mock.MockCryptoService
import com.google.gson.Gson
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue


class AuthenticationRepositoryImplTest {
    private lateinit var mockApiService: ApiService
    private lateinit var mockCryptoService: CryptoService
    private lateinit var repository: AuthenticationRepositoryImpl
    private lateinit var userDao: UserDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val mockDatabase = Room
            .inMemoryDatabaseBuilder(context, PocketGoodsDatabase::class.java)
            .addTypeConverter(ControlledConverter(Gson()))
            .build()

        mockApiService = ApiService(ApiMockEngine().get())

        mockCryptoService = MockCryptoService()

        repository = AuthenticationRepositoryImpl(
            database = mockDatabase,
            apiService = mockApiService,
            cryptoService = mockCryptoService
        )

        userDao = mockDatabase.userDao()
    }

    @Test
    fun login_should_authenticate_user_and_get_correct_user_data(): Unit = runTest {
        val email = MockData.MOCK_EMAIL
        val password = MockData.MOCK_PASSWORD

        repository.login(
            email = email,
            password = password
        ).test {
            val user = awaitItem()
            assertNotNull(user)
            val cachedUser = userDao
                .getCachedUserWithWishlist(user.id)
                .first()

            assertEquals(
                user,
                MockData
                    .loginResponse
                    .data
                    .user
                    .toUser()
            )

            assertNotNull(cachedUser)

            assertEquals(
                user,
                cachedUser.toUser()
            )
        }
    }

    @Test
    fun login_user_will_fail_after_wrong_credentials(): Unit = runTest {
        val email = "some wrong email"
        val password = "some wrong password"

        repository.login(
            email = email,
            password = password
        ).catch { error ->
            assertTrue(error is ApiError)
        }
    }

    @Test
    fun getAuthenticatedUser_should_get_the_current_authenticated_user(): Unit = runTest {
        userDao.insertUser(
            user = MockData
                .userDTO
                .toUserEntity(
                    token = mockCryptoService.encrypt(
                        data = MockData.TOKEN
                    )
                )
        )

        repository.getAuthenticatedUser().test {
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)

            val authenticatedUser = successState.data
            assertNotNull(authenticatedUser)
            assertEquals(
                authenticatedUser,
                MockData
                    .authenticatedUserResponse(
                        hasCorrectToken = true
                    )
                    .data
                    ?.toUser()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getAuthenticatedUser_should_get_no_user_after_wrong_token(): Unit = runTest {
        val wrongToken = "some wrong token"

        userDao.insertUser(
            user = MockData
                .userDTO
                .toUserEntity(
                    token = mockCryptoService.encrypt(
                        data = wrongToken
                    )
                )
        )

        repository.getAuthenticatedUser().test {
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is Resource.Error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun logout_should_logout_the_current_authenticated_user(): Unit = runTest {
        val user = MockData.userDTO

        userDao.insertUser(
            user = user.toUserEntity(
                token = mockCryptoService.encrypt(
                    data = MockData.TOKEN
                )
            )
        )

        val logoutResponse = repository.logout(userId = user.id)
        assertTrue(logoutResponse)
        assertNull(
            userDao.getLatestCachedUser().first()
        )
    }

    @Test
    fun logout_should_not_logout_the_current_authenticated_user_after_wrong_token(): Unit =
        runTest {
            val wrongToken = "some wrong token"
            val user = MockData.userDTO
            userDao.insertUser(
                user = user.toUserEntity(
                    token = mockCryptoService.encrypt(
                        data = wrongToken
                    )
                )
            )

            try {
                repository.logout(userId = user.id)
            } catch (exp: Exception) {
                assertTrue(exp is ApiError)
            }
        }
}