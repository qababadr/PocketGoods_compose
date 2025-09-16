package com.badrqaba.product_feature.data.di

import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.util.api.ApiService
import com.badrqaba.product_feature.data.repository.ProductRepositoryImpl
import com.badrqaba.product_feature.domain.repository.ProductRepository
import com.badrqaba.product_feature.domain.use_case.GetProductUseCase
import com.badrqaba.product_feature.domain.use_case.GetProductsUseCase
import com.badrqaba.product_feature.domain.use_case.GetSuggestedProductsUseCase
import com.badrqaba.product_feature.domain.use_case.ProductUseCases
import com.badrqaba.product_feature.domain.use_case.SearchProductsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductFeatureDomainModule {


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
    fun providesProductFeatureUseCases(
        repository: ProductRepository
    ): ProductUseCases {
        return ProductUseCases(
            getProducts = GetProductsUseCase(
                repository = repository
            ),
            getProduct = GetProductUseCase(repository = repository),
            searchProducts = SearchProductsUseCase(repository = repository),
            getSuggestedProducts = GetSuggestedProductsUseCase(repository = repository)
        )
    }
}