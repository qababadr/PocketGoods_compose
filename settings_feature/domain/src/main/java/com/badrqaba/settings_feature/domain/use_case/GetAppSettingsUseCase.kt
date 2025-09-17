package com.badrqaba.settings_feature.domain.use_case

import com.badrqaba.core.domain.model.AppSettings
import com.badrqaba.settings_feature.domain.repository.ApplicationSettingsRepository
import kotlinx.coroutines.flow.Flow


class GetAppSettingsUseCase(private val repository: ApplicationSettingsRepository) {

    operator fun invoke(): Flow<AppSettings> {
        return repository.getAppSettings()
    }
}