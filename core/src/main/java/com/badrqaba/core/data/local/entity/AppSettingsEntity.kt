package com.badrqaba.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.badrqaba.core.util.APP_SETTINGS_TABLE

@Entity(tableName = APP_SETTINGS_TABLE)
data class AppSettingsEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(name = "is_dark_mode")
    val isDarkMode: Boolean
)
