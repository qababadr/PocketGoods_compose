package com.badrqaba.settings_feature.domain.repository

import com.badrqaba.core.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface ApplicationSettingsRepository {

    fun getAppSettings(): Flow<AppSettings>


    suspend fun saveAppSettings(settings: AppSettings)
}