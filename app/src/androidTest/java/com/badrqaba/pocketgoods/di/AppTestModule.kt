package com.badrqaba.pocketgoods.di

import android.app.Application
import androidx.room.Room
import com.badrqaba.authentication_feature.data.repository.AuthenticationRepositoryImpl
import com.badrqaba.authentication_feature.domain.repository.AuthenticationRepository
import com.badrqaba.authentication_feature.domain.use_case.AuthenticationUseCases
import com.badrqaba.authentication_feature.domain.use_case.ClearCachedUserUseCase
import com.badrqaba.authentication_feature.domain.use_case.GetAuthenticatedUserUseCase
import com.badrqaba.authentication_feature.domain.use_case.GetCachedAuthenticatedUser
import com.badrqaba.authentication_feature.domain.use_case.LoginUseCase
import com.badrqaba.authentication_feature.domain.use_case.LogoutUseCase
import com.badrqaba.authentication_feature.domain.use_case.RegisterUseCase
import com.badrqaba.core.data.local.converter.ControlledConverter
import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.util.api.ApiService
import com.badrqaba.core.util.api.mock.ApiMockEngine
import com.badrqaba.core.util.crypto.CryptoService
import com.badrqaba.core.util.crypto.mock.MockCryptoService
import com.badrqaba.core.util.dispatcher.DispatcherProvider
import com.badrqaba.product_feature.data.repository.ProductRepositoryImpl
import com.badrqaba.product_feature.domain.repository.ProductRepository
import com.badrqaba.product_feature.domain.use_case.GetProductUseCase
import com.badrqaba.product_feature.domain.use_case.GetProductsUseCase
import com.badrqaba.product_feature.domain.use_case.GetSuggestedProductsUseCase
import com.badrqaba.product_feature.domain.use_case.ProductUseCases
import com.badrqaba.product_feature.domain.use_case.SearchProductsUseCase
import com.badrqaba.settings_feature.data.repository.ApplicationSettingsRepositoryImpl
import com.badrqaba.settings_feature.domain.repository.ApplicationSettingsRepository
import com.badrqaba.settings_feature.domain.use_case.ApplicationSettingsUseCases
import com.badrqaba.settings_feature.domain.use_case.GetAppSettingsUseCase
import com.badrqaba.settings_feature.domain.use_case.SaveAppSettingsUseCase
import com.badrqaba.wishlist_feature.data.repository.WishlistRepositoryImpl
import com.badrqaba.wishlist_feature.domain.repository.WishlistRepository
import com.badrqaba.wishlist_feature.domain.use_case.GetEntireWishlistUseCase
import com.badrqaba.wishlist_feature.domain.use_case.ToggleWishlistUseCase
import com.badrqaba.wishlist_feature.domain.use_case.WishlistUseCases
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.engine.HttpClientEngine
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppTestModule {

    @Provides
    @Singleton
    fun providesCoroutineTestDispatcher(): DispatcherProvider{
        return EndToEndTestDispatchers()
    }

    @Provides
    @Singleton
    fun providesPocketGoodsMemoryDatabase(application: Application): PocketGoodsDatabase {
        return Room
            .inMemoryDatabaseBuilder(application, PocketGoodsDatabase::class.java)
            .addTypeConverter(ControlledConverter(Gson()))
            .build()
    }

    @Provides
    @Singleton
    fun providesApiServiceMockEngine(): HttpClientEngine {
        return ApiMockEngine().get()
    }

    @Provides
    @Singleton
    fun providesApiService(engine: HttpClientEngine): ApiService {
        return ApiService(engine = engine)
    }

    @Provides
    @Singleton
    fun provideCryptoService(): CryptoService {
        return MockCryptoService()
    }

    @Provides
    @Singleton
    fun providesProductFeatureRepository(
        database: PocketGoodsDatabase,
        apiService: ApiService
    ): ProductRepository {
        return ProductRepositoryImpl(
            database = database,
            apiService = apiService
        )
    }

    @Provides
    @Singleton
    fun providesProductFeatureUseCases(repository: ProductRepository): ProductUseCases {
        return ProductUseCases(
            getProducts = GetProductsUseCase(repository = repository),
            getProduct = GetProductUseCase(repository = repository),
            searchProducts = SearchProductsUseCase(repository = repository),
            getSuggestedProducts = GetSuggestedProductsUseCase(repository = repository)
        )
    }

    @Provides
    @Singleton
    fun providesAuthenticationRepository(
        database: PocketGoodsDatabase,
        apiService: ApiService,
        cryptoService: CryptoService
    ): AuthenticationRepository {
        return AuthenticationRepositoryImpl(
            database = database,
            apiService = apiService,
            cryptoService = cryptoService
        )
    }

    @Provides
    @Singleton
    fun providesAuthenticationUseCases(
        repository: AuthenticationRepository
    ): AuthenticationUseCases {
        return AuthenticationUseCases(
            login = LoginUseCase(repository = repository),
            register = RegisterUseCase(repository = repository),
            getAuthenticatedUser = GetAuthenticatedUserUseCase(repository = repository),
            logout = LogoutUseCase(repository = repository),
            clearCachedUser = ClearCachedUserUseCase(repository = repository),
            getCachedAuthenticatedUser = GetCachedAuthenticatedUser(repository = repository)
        )
    }

    @Provides
    @Singleton
    fun providesWishlistRepository(
        database: PocketGoodsDatabase,
        apiService: ApiService,
        cryptoService: CryptoService
    ): WishlistRepository {
        return WishlistRepositoryImpl(
            database = database,
            apiService = apiService,
            cryptoService = cryptoService
        )
    }

    @Provides
    @Singleton
    fun providesWishlistUseCases(
        repository: WishlistRepository
    ): WishlistUseCases {
        return WishlistUseCases(
            toggleWishlist = ToggleWishlistUseCase(repository = repository),
            getEntireWishlist = GetEntireWishlistUseCase(repository = repository)
        )
    }

    @Provides
    @Singleton
    fun providesApplicationSettingsRepository(
        database: PocketGoodsDatabase
    ): ApplicationSettingsRepository {
        return ApplicationSettingsRepositoryImpl(
            database = database
        )
    }

    @Provides
    @Singleton
    fun providesApplicationSettingsUseCases(
        repository: ApplicationSettingsRepository
    ): ApplicationSettingsUseCases {
        return ApplicationSettingsUseCases(
            getApplicationSettings = GetAppSettingsUseCase(repository = repository),
            saveApplicationSettings = SaveAppSettingsUseCase(repository = repository)
        )
    }
}