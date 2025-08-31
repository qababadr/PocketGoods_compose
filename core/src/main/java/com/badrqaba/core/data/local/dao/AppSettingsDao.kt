package com.badrqaba.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.badrqaba.core.data.local.entity.AppSettingsEntity
import com.badrqaba.core.util.APP_SETTINGS_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface AppSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAppSettings(appSettings: AppSettingsEntity)

    @Query("SELECT * FROM $APP_SETTINGS_TABLE LIMIT 1")
    fun getAppSettings(): Flow<AppSettingsEntity?>

}