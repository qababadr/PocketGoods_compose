package com.badrqaba.wishlist_feature.data.di

import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.util.api.ApiService
import com.badrqaba.core.util.crypto.CryptoService
import com.badrqaba.wishlist_feature.data.repository.WishlistRepositoryImpl
import com.badrqaba.wishlist_feature.domain.repository.WishlistRepository
import com.badrqaba.wishlist_feature.domain.use_case.GetEntireWishlistUseCase
import com.badrqaba.wishlist_feature.domain.use_case.ToggleWishlistUseCase
import com.badrqaba.wishlist_feature.domain.use_case.WishlistUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WishlistFeatureDomainModule {

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
}