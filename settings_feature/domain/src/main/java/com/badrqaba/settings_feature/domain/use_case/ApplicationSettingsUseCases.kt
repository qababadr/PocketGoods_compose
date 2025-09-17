package com.badrqaba.settings_feature.domain.use_case

data class ApplicationSettingsUseCases(
    val getApplicationSettings: GetAppSettingsUseCase,
    val saveApplicationSettings: SaveAppSettingsUseCase
)
