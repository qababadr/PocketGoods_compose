package com.badrqaba.core.data.mapper

import com.badrqaba.core.data.local.entity.AppSettingsEntity
import com.badrqaba.core.domain.model.AppSettings

fun AppSettingsEntity.toAppSettings(): AppSettings {
    return AppSettings(isDarkMode)
}

fun AppSettings.toAppSettingsEntity(): AppSettingsEntity {
    return AppSettingsEntity(0, isDarkMode)
}