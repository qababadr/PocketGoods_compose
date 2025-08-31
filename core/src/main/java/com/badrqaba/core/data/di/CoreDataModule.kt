package com.badrqaba.core.data.di

import android.app.Application
import androidx.room.Room
import com.badrqaba.core.data.local.converter.ControlledConverter
import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.util.DATABASE_NAME
import com.badrqaba.core.util.api.ApiService
import com.badrqaba.core.util.crypto.CryptoService
import com.badrqaba.core.util.crypto.CryptoServiceImpl
import com.badrqaba.core.util.dispatcher.DefaultDispatchers
import com.badrqaba.core.util.dispatcher.DispatcherProvider
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreDataModule {

    @Provides
    @Singleton
    fun providesCoroutineDispatchers(): DispatcherProvider {
        return DefaultDispatchers()
    }

    @Provides
    @Singleton
    fun providePocketGoodsDatabase(application: Application): PocketGoodsDatabase {
        return Room.databaseBuilder(
            application,
            PocketGoodsDatabase::class.java,
            DATABASE_NAME
        ).addTypeConverter(ControlledConverter(Gson()))
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpClientEngine(): HttpClientEngine {
        return CIO.create()
    }

    @Provides
    @Singleton
    fun provideApiService(engine: HttpClientEngine): ApiService {
        return ApiService(engine)
    }

    @Provides
    @Singleton
    fun provideCryptoService(): CryptoService = CryptoServiceImpl()
}