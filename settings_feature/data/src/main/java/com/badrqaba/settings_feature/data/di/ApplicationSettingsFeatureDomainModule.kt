package com.badrqaba.settings_feature.data.di

import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.settings_feature.data.repository.ApplicationSettingsRepositoryImpl
import com.badrqaba.settings_feature.domain.repository.ApplicationSettingsRepository
import com.badrqaba.settings_feature.domain.use_case.ApplicationSettingsUseCases
import com.badrqaba.settings_feature.domain.use_case.GetAppSettingsUseCase
import com.badrqaba.settings_feature.domain.use_case.SaveAppSettingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationSettingsFeatureDomainModule {

    @Provides
    @Singleton
    fun providesApplicationSettingsRepository(
        database: PocketGoodsDatabase
    ): ApplicationSettingsRepository {
        return ApplicationSettingsRepositoryImpl(database = database)
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