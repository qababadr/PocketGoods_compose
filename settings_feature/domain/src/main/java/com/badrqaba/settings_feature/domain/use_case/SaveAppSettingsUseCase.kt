package com.badrqaba.settings_feature.domain.use_case

import com.badrqaba.core.domain.model.AppSettings
import com.badrqaba.settings_feature.domain.repository.ApplicationSettingsRepository


class SaveAppSettingsUseCase(private val repository: ApplicationSettingsRepository) {

    suspend operator fun invoke(settings: AppSettings) {
        repository.saveAppSettings(settings = settings)
    }
}