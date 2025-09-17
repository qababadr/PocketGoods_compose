package com.badrqaba.settings_feature.data.repository

import com.badrqaba.core.data.local.database.PocketGoodsDatabase
import com.badrqaba.core.data.mapper.toAppSettings
import com.badrqaba.core.data.mapper.toAppSettingsEntity
import com.badrqaba.core.domain.model.AppSettings
import com.badrqaba.settings_feature.domain.repository.ApplicationSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ApplicationSettingsRepositoryImpl @Inject constructor(
    database: PocketGoodsDatabase
) : ApplicationSettingsRepository {

    private val dao = database.appSettingsDao()

    override fun getAppSettings(): Flow<AppSettings> {
        return dao
            .getAppSettings()
            .map {
                it?.toAppSettings() ?: AppSettings.DEFAULT
            }
    }

    override suspend fun saveAppSettings(settings: AppSettings) {
        dao.saveAppSettings(
            appSettings = settings.toAppSettingsEntity()
        )
    }
}