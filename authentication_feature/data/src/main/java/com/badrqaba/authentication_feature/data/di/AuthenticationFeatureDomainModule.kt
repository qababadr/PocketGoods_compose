package com.badrqaba.authentication_feature.data.di

import com.badrqaba.authentication_feature.data.repository.AuthenticationRepositoryImpl
import com.badrqaba.authentication_feature.domain.repository.AuthenticationRepository
import com.badrqaba.authentication_feature.domain.use_case.AuthenticationUseCases
import com.badrqaba.authentication_feature.domain.use_case.ClearCachedUserUseCase
import com.badrqaba.authentication_feature.domain.use_case.GetAuthenticatedUserUseCase
import com.badrqaba.authentication_feature.domain.use_case.GetCachedAuthenticatedUser
import com.badrqaba.authentication_feature.domain.use_case.LoginUseCase
import com.badrqaba.authentication_feature.domain.use_case.LogoutUseCase
import com.badrqaba.authentication_feature.domain.use_case.RegisterUseCase
import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.util.api.ApiService
import com.badrqaba.core.util.crypto.CryptoServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationFeatureDomainModule {

    @Provides
    @Singleton
    fun providesAuthenticationRepository(
        database: PocketGoodsDatabase,
        apiService: ApiService,
        cryptoService: CryptoServiceImpl
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
}